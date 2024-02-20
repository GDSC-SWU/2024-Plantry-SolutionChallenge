package com.plantry.presentation.addfood.bottomsheet

import androidx.fragment.app.viewModels
import com.plantry.R
import com.plantry.coreui.base.BindingBottomSheetFragment
import com.plantry.data.dto.response.product.ResponseProductIconListDto
import com.plantry.databinding.BottomsheetAddFoodIconSelectBinding
import com.plantry.presentation.addfood.adapter.FoodGroupNameListAdapter
import com.plantry.presentation.addfood.adapter.OnFoodItemClickListener
import com.plantry.presentation.addfood.rawdata.FoodIconList
import com.plantry.presentation.addfood.viewmodel.product.FoodViewModel

class AddFoodIconSelectBottomSheet :
    BindingBottomSheetFragment<BottomsheetAddFoodIconSelectBinding>(
        R.layout.bottomsheet_add_food_icon_select
    ),
    OnFoodItemClickListener {
    private val viewModelFood by viewModels<FoodViewModel>({requireActivity()})

    override fun initView() {
        val adapter = FoodGroupNameListAdapter(this)
        binding.rcvAddFoodIconList.adapter = adapter
        adapter.submitList(FoodIconList.foodLists)
    }

    override fun onItemClick(item: ResponseProductIconListDto.Food) {
        viewModelFood.setSucessIcon(icon = item.icon)
        viewModelFood.setSucessName(name = item.name)
        dismiss()
    }
}
