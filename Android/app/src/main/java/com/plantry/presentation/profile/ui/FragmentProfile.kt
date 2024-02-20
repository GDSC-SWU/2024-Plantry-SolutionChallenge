package com.plantry.presentation.profile.ui


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.plantry.R
import com.plantry.coreui.adapter.ItemClick
import com.plantry.coreui.base.BindingFragment
import com.plantry.coreui.view.UiState
import com.plantry.data.dto.response.profile.ResponseProfileMissionListDto
import com.plantry.databinding.FragmentProfileBinding
import com.plantry.presentation.profile.adapter.ProfileMissionViewPagerAdapter
import com.plantry.presentation.profile.popup.DeleteFoodPopUp
import com.plantry.presentation.profile.popup.LogOutPopUp
import com.plantry.presentation.profile.viewmodel.ProfileInfoViewModel
import com.plantry.presentation.profile.viewmodel.ProfileMissionListViewModel
import com.plantry.presentation.profile.viewmodel.ProfileMissionSuccessViewModel
import com.plantry.presentation.profile.viewmodel.ProfileTrackerViewModel

class FragmentProfile : BindingFragment<FragmentProfileBinding>(R.layout.fragment_profile) {
    private val viewModelMissionList by viewModels<ProfileMissionListViewModel>()
    private val viewModelMissionSuccess by viewModels<ProfileMissionSuccessViewModel>()
    private val viewModelInfo by viewModels<ProfileInfoViewModel>()
    private val viewModelTracker by viewModels<ProfileTrackerViewModel>()
    var nowSelectedItem = 0
    val firstChartData =
        listOf(
            PieEntry(0F),
            PieEntry(0F),
            PieEntry(0F),
            PieEntry(0F),
        )

   var userImgPath: String = ""
    override fun initView() {
        setChartData(firstChartData)
        observeMissionList()
        observeMissionSuccess()
        viewModelInfo.getInfoProfile()
        observeProfileInfo()
        viewModelTracker.getTrackerProfile()
        observeTracker()
        clickAboutUs()
        clickLogOut()
        clickDeleteFood()
        clickChartItem()
        clickEditProfile()
        clickTerms()
        clickDeleteId()
        clickAlramCheck()
    }

    private fun setChartData(entries: List<PieEntry>) {
        binding.pcProfileDeleteFoodChart.animateY(0, Easing.EaseInOutQuad)

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = mutableListOf(
            ContextCompat.getColor(requireContext(), R.color.cover_yellow),
            ContextCompat.getColor(requireContext(), R.color.cover_red),
            ContextCompat.getColor(requireContext(), R.color.cover_green),
            ContextCompat.getColor(requireContext(), R.color.cover_purple),
        )
        val data = PieData(dataSet)
        data.setDrawValues(false)
        binding.pcProfileDeleteFoodChart.data = data
        binding.pcProfileDeleteFoodChart.description.isEnabled = false
        binding.pcProfileDeleteFoodChart.legend.isEnabled = false
        binding.pcProfileDeleteFoodChart.holeRadius = 75f
        binding.pcProfileDeleteFoodChart.transparentCircleRadius = 75f
        binding.pcProfileDeleteFoodChart.setHoleColor(Color.TRANSPARENT) // 투명한 원 내부를 투명하게 설정
    }

    private fun clickEditProfile(){
        binding.tvProfileUserEmail.setOnClickListener {
            goToEditProfile()
        }
        binding.tvProfileUserName.setOnClickListener {
            goToEditProfile()
        }
    }

    private fun goToEditProfile(){
        arguments = Bundle().apply {
            putString("user_email", binding.tvProfileUserEmail.text.toString())
            putString("user_img", userImgPath)
            putString("user_name", binding.tvProfileUserName.text.toString())
        }
        findNavController().navigate(R.id.action_profile_to_edit, arguments)
    }

    private fun clickChartItem() {
        binding.pcProfileDeleteFoodChart.setOnChartValueSelectedListener(object :
            OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                when(h?.x){
                    0F -> { // Ingetion
                        binding.tvProfileDeleteSort.text = INGESTION
                    }
                    1F -> { // disposal
                        binding.tvProfileDeleteSort.text = DISPOSAL
                    }
                    2F -> { // sharing
                        binding.tvProfileDeleteSort.text = SHARE
                    }
                    3F -> { // mistake
                        binding.tvProfileDeleteSort.text = MISTAKE
                    }
                }

                binding.tvProfilePercent.text ="${h?.y} %"
            }

            override fun onNothingSelected() {

            }
        })
    }

    private fun clickItem(
        adapter: ProfileMissionViewPagerAdapter,
        list: List<ResponseProfileMissionListDto.Result?>
    ) {
        adapter.missionNameClick = object : ItemClick {
            override fun onClick(view: View, position: Int) {
                if (view.id == R.id.tv_profile_mission_item_name) {
                    list[position]?.missionId?.let {
                        viewModelMissionSuccess.patchMissionSuccessProfile(it)
                    }
                }
            }
        }
    }

    private fun clickAboutUs() {
        binding.tvProfileAboutUsContent.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_about_us)
        }
    }
    private fun clickAlramCheck(){
        binding.tvProfileNotificationContent.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_alarm)
        }
    }
    private fun clickDeleteId(){
        binding.tvProfileDeleteIdContent.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_delete)
        }
    }

    private fun clickTerms(){
        binding.tvProfileTermUseContent.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_terms)
        }
    }
    private fun clickLogOut() {
        binding.tvProfileLogOutContent.setOnClickListener {
            val logoutPopUp = LogOutPopUp()
            logoutPopUp.setStyle(
                BottomSheetDialogFragment.STYLE_NO_TITLE,
                R.style.Theme_Plantry_AlertDialog
            )
            logoutPopUp.show(parentFragmentManager, POP_UP_LOGOUT)
        }
    }

    private fun clickDeleteFood() {
        binding.tvProfileDeleteFood.setOnClickListener {
            val deleteFoodPopUp = DeleteFoodPopUp()
            deleteFoodPopUp.setStyle(
                BottomSheetDialogFragment.STYLE_NO_TITLE,
                R.style.Theme_Plantry_AlertDialog
            )
            deleteFoodPopUp.show(parentFragmentManager, POP_UP_HELP)

        }
    }

    private fun observeProfileInfo() {
        viewModelInfo.infoItem.observe(this) { it ->
            when (it) {
                is UiState.Success -> {
                    if (!(it.data.profileImagePath.isNullOrBlank())) {
                        userImgPath = it.data.profileImagePath
                        binding.ivProfileProfileImg.load(it.data.profileImagePath) {
                            size(dpToPx(requireContext(), 52))
                            transformations(CircleCropTransformation())
                        }
                    }else{
                        binding.ivProfileProfileImg.background = resources.getDrawable(R.drawable.ic_profile_user_basic_img)
                    }
                    binding.tvProfileUserName.text = it.data.nickname
                    binding.tvProfileUserEmail.text = it.data.email
                }

                else -> Unit
            }
        }
    }
    fun dpToPx(context: Context, dp: Int): Int {
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        )
        return px.toInt()
    }
    private fun observeTracker() {
        viewModelTracker.trackerItem.observe(this) {
            when (it) {
                is UiState.Success -> {
                    val ingestion = it.data.Ingestion.toString().toFloat()
                    val disposal = it.data.Disposal.toString().toFloat()
                    val sharing = it.data.Share.toString().toFloat()
                    val mistake = it.data.Mistake.toString().toFloat()
                     binding.tvProfileDeleteSort.text = DISPOSAL

                    if (!(ingestion == 0F &&
                        disposal == 0F &&
                        sharing == 0F &&
                        mistake == 0F)
                    ){
                        binding.tvProfilePercent.text ="${disposal} %"
                        setChartData(
                            listOf(
                                PieEntry(ingestion,),
                                PieEntry(disposal),
                                PieEntry(sharing),
                                PieEntry(mistake),
                            )
                        )
                    }
                    else{
                        binding.tvProfilePercent.text ="${0.0} %"
                    }


                }

                else -> Unit
            }
        }
    }

    private fun observeMissionList() {
        viewModelMissionList.missionItem.observe(this) { it ->
            when (it) {
                is UiState.Success -> {
                    val adapter = ProfileMissionViewPagerAdapter()
                    binding.vpProfileMission.adapter = adapter
                    adapter.submitList(it.data)
                    clickItem(adapter, it.data)
                    binding.vpProfileMission.setCurrentItem(nowSelectedItem, false)
                    binding.ciProfileCircle.setViewPager(binding.vpProfileMission)
                }

                else -> Unit
            }
        }
    }

    private fun observeMissionSuccess() {
        viewModelMissionSuccess.missionSuccessItem.observe(this) { it ->
            when (it) {
                is UiState.Success -> {
                    nowSelectedItem = binding.vpProfileMission.currentItem
                    viewModelMissionList.getMissionListProfile()
                }

                else -> Unit
            }
        }
    }

    companion object {
        const val DISPOSAL = "Disposal"
        const val INGESTION = "Ingestion"
        const val MISTAKE = "Mistake"
        const val SHARE = "Sharing"
        const val POP_UP_LOGOUT = "profile_to_logout"
        const val POP_UP_HELP = "profile_to_help"
    }
}