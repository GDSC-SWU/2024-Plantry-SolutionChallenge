package com.plantry.presentation.profile.ui

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.plantry.R
import com.plantry.coreui.base.BindingFragment
import com.plantry.coreui.fragment.longToast
import com.plantry.coreui.view.UiState
import com.plantry.databinding.FragmentProfileEditBinding
import com.plantry.presentation.profile.popup.LogOutPopUp
import com.plantry.presentation.profile.viewmodel.ProfileNameChangeViewModel

class FragmentProfileEdit :
    BindingFragment<FragmentProfileEditBinding>(R.layout.fragment_profile_edit) {
    override var bnvVisibility = View.GONE

    private val viewModelNameChange by viewModels<ProfileNameChangeViewModel>()

    private var currentNickname: String = ""

    override fun initView() {
        setProfile()
        clickConfirm()
        clickLogout()
        observeLogOut()
        clickBackStack()
        clickDeletId()
    }

    private fun clickBackStack(){
        binding.ivProfileEditToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setProfile() {
        val email = arguments?.getString("user_email")
        val imgPath = arguments?.getString("user_img")
        val name = arguments?.getString("user_name")
        if (name != null) {
            currentNickname = name
        }
        Log.d("aaa", email+ imgPath+name)
        binding.etProfileNicknameContent.setText(name)
        binding.tvProfileEmailContent.setText(email)
        if (!(imgPath.isNullOrBlank())) {
            binding.ivProfileEditImg.load(imgPath) {
                size(dpToPx(requireContext(), 180))
                transformations(CircleCropTransformation())
            }
        } else {
            binding.ivProfileEditImg.background =
                resources.getDrawable(R.drawable.ic_edit_profile_charcater)
        }
    }

    private fun clickConfirm() {
        binding.tvProfileEditSave.setOnClickListener {
            val newNickname = binding.etProfileNicknameContent.text.toString().trim()

            if (newNickname.isNotEmpty() && newNickname != currentNickname) {
                viewModelNameChange.patchNameChangeProfile(newNickname)
                currentNickname = newNickname
            }
        }
    }

    private fun clickLogout() {
        binding.tvProfileEditLogOut.setOnClickListener {
            val logoutPopUp = LogOutPopUp()
            logoutPopUp.setStyle(
                BottomSheetDialogFragment.STYLE_NO_TITLE,
                R.style.Theme_Plantry_AlertDialog
            )
            logoutPopUp.show(parentFragmentManager, FragmentProfile.POP_UP_LOGOUT)
        }

    }

    private fun clickDeletId() {
        binding.tvProfileEditDeleteId.setOnClickListener {
            findNavController().navigate(R.id.action_profile_edit_name_to_delete)
        }

    }

    private fun observeLogOut() {
        viewModelNameChange.nameItem.observe(this) { it ->
            when (it) {
                is UiState.Success -> {
                    binding.etProfileNicknameContent.setText(it.data)
                    longToast("Your nickname has been changed successfully.\n")
                }

                else -> Unit
            }
        }
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        )
        return px.toInt()
    }
}