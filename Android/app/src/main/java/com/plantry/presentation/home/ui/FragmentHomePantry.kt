package com.plantry.presentation.home.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.plantry.R
import com.plantry.coreui.base.BindingFragment
import com.plantry.coreui.view.UiState
import com.plantry.data.dto.response.product.ResponseProductListDto
import com.plantry.databinding.FragmentHomePantryBinding
import com.plantry.presentation.addfood.popup.addfood.AddFoodPopUp
import com.plantry.presentation.home.adapter.pantry.OnItemClickListener
import com.plantry.presentation.home.adapter.pantry.PantryDayAdapter
import com.plantry.presentation.home.bottomsheet.HomeAlarmBottomSheet
import com.plantry.presentation.home.viewmodel.product.ProductDeleteViewModel
import com.plantry.presentation.home.viewmodel.product.ProductEditCountViewModel
import com.plantry.presentation.home.viewmodel.product.ProductListSearchViewModel
import com.plantry.presentation.home.viewmodel.product.ProductSearchViewModel


class FragmentHomePantry :
    BindingFragment<FragmentHomePantryBinding>(R.layout.fragment_home_pantry),
    OnItemClickListener {
    private val viewModelList by viewModels<ProductListSearchViewModel>({ requireParentFragment() })
    private val viewModelSearch by viewModels<ProductSearchViewModel>()
    private val viewModelEditCount by viewModels<ProductEditCountViewModel>()
    private val viewModelProuductDelete by viewModels<ProductDeleteViewModel>({ requireActivity() })

    override var bnvVisibility = View.GONE
    lateinit var searchKeyWord: String


    override fun initView() {
        setFilter()
        getSelectedFilter()
        setPantryName()
        backToHome()
        clickPlusProduct()
        clickEmptyPlusProduct()
        observeList()
        observeSearch()
        checkDateValidate()
        stopSearch()
        observeEditCount()
        observeDelete()
    }

    private fun setPantryName() {
        val pantryName = arguments?.getString("pantry_name")
        binding.tvHomePantryName.setText(pantryName)
    }

    private fun backToHome() {
        binding.ivHomePantryBack.setOnClickListener {
            findNavController().navigate(R.id.action_home_pantry_to_home)
        }
    }

    private fun clickPlusProduct() {
        binding.ivHomePantryPlusFood.setOnClickListener {
            goToAddProduct()
        }
    }

    private fun clickEmptyPlusProduct() {
        binding.layoutHomePantryEmptyView.tvHomePantryEmptyAddFood.setOnClickListener {
            goToAddProduct()
        }
    }

    private fun goToAddProduct() {
        val pantryId = arguments?.getInt("pantry_id")
        val pantryName = arguments?.getString("pantry_name")
        val addFoodPopUp = AddFoodPopUp()
        addFoodPopUp.setStyle(
            BottomSheetDialogFragment.STYLE_NO_TITLE, R.style.Theme_Plantry_AlertDialog
        )
        addFoodPopUp.arguments = Bundle().apply {
            putInt("mode", FOR_ADD_FROM_BASE)
            if (pantryId != null) {
                putInt("pantry_id", pantryId)
                putString("pantry_name", pantryName)
                putString("pantry_filter", getFilter(binding.tlHomePantryTab.selectedTabPosition))
            }
        }
        addFoodPopUp.show(parentFragmentManager, ADD_POP_UP)
    }

    override fun onItemClick(item: ResponseProductListDto.Result.Food) {
        val pantryId = arguments?.getInt("pantry_id")
        val pantryName = arguments?.getString("pantry_name")
        val addFoodPopUp = AddFoodPopUp()
        addFoodPopUp.setStyle(
            DialogFragment.STYLE_NO_TITLE,
            R.style.Theme_Plantry_AlertDialog
        )
        val bundle = Bundle().apply {
            putInt("mode", FOR_EDIT)
            putInt("productId", item.productId ?: 0)
            putDouble("count", item.count ?: 0.0)
            putInt("days", item.days ?: 0)
            putString("icon", item.icon.orEmpty())
            if (item.isUseBydate == true) {
                putInt("isUseBydate", 1)
            } else {
                putInt("isUseBydate", 2)
            }
            putString("product_storage", item.storage)
            putString("pantry_name", pantryName)
            putString("products_name", item.name)
            if (pantryId != null) {
                putInt("pantry_id", pantryId)
            }
            putString("pantry_filter", getFilter(binding.tlHomePantryTab.selectedTabPosition))
        }
        addFoodPopUp.arguments = bundle
        addFoodPopUp.show(parentFragmentManager, ADD_POP_UP)
    }

    override fun onAlarmClick(item: ResponseProductListDto.Result.Food) {
        val pantryId = arguments?.getInt("pantry_id")
        val pantryFilter = getFilter(binding.tlHomePantryTab.selectedTabPosition)
        val alarmBottomSheet = HomeAlarmBottomSheet()
        alarmBottomSheet.arguments = Bundle().apply {
            if (pantryId != null && item.productId != null) {
                putInt("pantryId", pantryId)
                putString("pantryFilter", pantryFilter)
                putInt("productId", item.productId)
            }
        }
        alarmBottomSheet.show(parentFragmentManager, BOTTOM_SHEET)
    }

    override fun onPlusClick(item: ResponseProductListDto.Result.Food) {
        val editCount = item.count?.plus(1.0)
        if (item.productId != null && editCount != null) {
            viewModelEditCount.patchEditCountProduct(item.productId, editCount)
        }
    }

    override fun onMinusClick(item: ResponseProductListDto.Result.Food) {
        val editCount = item.count?.minus(1.0)
        if (item.productId != null && editCount != null) {
            viewModelEditCount.patchEditCountProduct(item.productId, editCount)
        }
    }

    private fun checkDateValidate() {
        val pantryId = arguments?.getInt("pantry_id")
        binding.etHomePantrySearch.addTextChangedListener(object : TextWatcher {
            val pantryFilter = getFilter(binding.tlHomePantryTab.selectedTabPosition)
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.ivHomePantrySearchClose.visibility = View.VISIBLE
            }

            override fun afterTextChanged(text: Editable?) {
                if (pantryId != null) {
                    if (text.toString().isEmpty()) {
                        viewModelList.getListSearchProduct(pantryId, pantryFilter)
                    } else {
                        if (text.toString().contains("\n")) {
                            searchKeyWord = binding.etHomePantrySearch.text.toString().trim()
                            viewModelSearch.getSearchProduct(
                                pantryId, pantryFilter, searchKeyWord
                            )
                            binding.etHomePantrySearch.setText(searchKeyWord)
                        }
                    }
                }
            }
        }
        )
    }

    private fun stopSearch() {
        val pantryId = arguments?.getInt("pantry_id")
        binding.ivHomePantrySearchClose.setOnClickListener {
            val pantryFilter = getFilter(binding.tlHomePantryTab.selectedTabPosition)
            binding.etHomePantrySearch.setText("")
            binding.ivHomePantrySearchClose.visibility = View.GONE
            if (pantryId != null) {
                viewModelList.getListSearchProduct(pantryId, pantryFilter)
            }
        }
    }

    private fun getFilter(selectedPosition: Int): String {
        when (selectedPosition) {
            0 -> return ALL
            1 -> return COLD
            2 -> return FREEZE
            3 -> return ETC
        }
        return ALL
    }

    private fun setEmptyViewVisibility(pantryListData: ResponseProductListDto) {
        val end: Int = pantryListData.result.size-1
        for (position in 0..end) {
            val subList = pantryListData.result[position]?.list?.size
            if (subList == 0) {
                binding.layoutHomePantryEmptyView.clHomePantryEmptyView.visibility = View.VISIBLE
                binding.rcvHomePantryItemFoodList.visibility = View.GONE
            } else {
                binding.layoutHomePantryEmptyView.clHomePantryEmptyView.visibility = View.GONE
                binding.rcvHomePantryItemFoodList.visibility = View.VISIBLE
                break
            }
        }

    }

    private fun setFilter() {
        val pantryId = arguments?.getInt("pantry_id")
        if (pantryId != null) {
            viewModelList.getListSearchProduct(pantryId, ALL)
        }

    }

    private fun getSelectedFilter() {
        val pantryId = arguments?.getInt("pantry_id")
        binding.tlHomePantryTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) { // 숫자로 비교할 수 있도록 변경해보기
                    ALL -> {
                        if (pantryId != null) {
                            if (binding.ivHomePantrySearchClose.visibility == View.VISIBLE) {
                                viewModelSearch.getSearchProduct(pantryId, ALL, searchKeyWord)
                            } else {
                                viewModelList.getListSearchProduct(pantryId, ALL)
                            }
                        }
                    }

                    COLD -> {
                        if (pantryId != null) {
                            if (binding.ivHomePantrySearchClose.visibility == View.VISIBLE) {
                                viewModelSearch.getSearchProduct(pantryId, COLD, searchKeyWord)
                            } else {
                                viewModelList.getListSearchProduct(pantryId, COLD)
                            }
                        }
                    }

                    FREEZE -> {
                        if (pantryId != null) {
                            if (binding.ivHomePantrySearchClose.visibility == View.VISIBLE) {
                                viewModelSearch.getSearchProduct(pantryId, FREEZE, searchKeyWord)
                            } else {
                                viewModelList.getListSearchProduct(pantryId, FREEZE)
                            }
                        }
                    }

                    "etc" -> {
                        if (pantryId != null) {
                            if (binding.ivHomePantrySearchClose.visibility == View.VISIBLE) {
                                viewModelSearch.getSearchProduct(pantryId, ETC, searchKeyWord)
                            } else {
                                viewModelList.getListSearchProduct(pantryId, ETC)
                            }
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun observeEditCount() {
        val pantryId = arguments?.getInt("pantry_id")
        viewModelEditCount.productEditCount.observe(this) {
            Log.d("aaass", "수정".toString())
            when (it) {
                is UiState.Success -> {
                    if (pantryId != null) {
                        viewModelList.getListSearchProduct(
                            pantryId,
                            getFilter(binding.tlHomePantryTab.selectedTabPosition)
                        )
                    }
                }

                else -> Unit
            }
        }
    }

    private fun observeDelete() {
        viewModelProuductDelete.productDelete.observe(this) {
            val pantryId = arguments?.getInt("pantry_id")
            when (it) {
                is UiState.Success -> {
                    if (pantryId != null) {
                        viewModelList.getListSearchProduct(
                            pantryId,
                            getFilter(binding.tlHomePantryTab.selectedTabPosition)
                        )
                    }
                }

                else -> Unit
            }
        }
    }
    private fun observeList() {
        viewModelList.productListSearch.observe(this) {
            when (it) {
                is UiState.Success -> {
                    val adapter = PantryDayAdapter(this)
                    binding.rcvHomePantryItemFoodList.adapter = adapter
                    val pantryList = it.data.result
                    setEmptyViewVisibility(it.data)
                    adapter.submitList(pantryList)
                    Log.d("aaa", pantryList.toString())
                }

                else -> Unit
            }
        }
    }

    private fun observeSearch() {
        viewModelSearch.productSearch.observe(this) {
            when (it) {
                is UiState.Success -> {
                    Log.d("aaass", it.data.toString())
                    val adapter = PantryDayAdapter(this)
                    binding.rcvHomePantryItemFoodList.adapter = adapter
                    val pantryList = it.data.result
                    setEmptyViewVisibility(it.data)
                    adapter.submitList(pantryList)
                    Log.d("aaa", pantryList.toString())
                }

                else -> Unit
            }
        }
    }

    companion object {
        const val ALL = "All"
        const val COLD = "Cold"
        const val FREEZE = "Freeze"
        const val ETC = "Etc"
        const val BOTTOM_SHEET = "home_pantry_to_alarm_bottom_sheet"
        const val ADD_POP_UP = "home_pantry_to_add_food_popup"
        const val FOR_EDIT = 1
        const val FOR_ADD_FROM_BASE = 2
        const val FOR_ADD_FROM_NO_BASE = 3
    }

}