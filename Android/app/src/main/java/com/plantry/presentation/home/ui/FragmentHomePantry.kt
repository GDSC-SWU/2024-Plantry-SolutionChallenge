package com.plantry.presentation.home.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.plantry.R
import com.plantry.coreui.base.BindingFragment
import com.plantry.data.dto.response.ResponseHomeDto
import com.plantry.data.dto.response.ResponseHomePantryDto
import com.plantry.databinding.FragmentHomePantryBinding
import com.plantry.presentation.addfood.popup.AddFoodPopUp
import com.plantry.presentation.addfood.ui.FragmentAddFood
import com.plantry.presentation.home.adapter.PantryAdapter
import com.plantry.presentation.home.adapter.PantryDayAdapter
import com.plantry.presentation.home.bottomsheet.HomeElseBottomSheet
import com.plantry.presentation.home.popup.HomePlusPopUp


class FragmentHomePantry :
    BindingFragment<FragmentHomePantryBinding>(R.layout.fragment_home_pantry) {

    override var bnvVisibility = View.GONE

    override fun initView() {
        setPantryName()
        backToHome()
        setRcvList()
        clickPlusFood()
    }

    private fun setPantryName() {
        val pantry_name = arguments?.getString("pantry_name")
        binding.tvHomePantryName.setText(pantry_name)
    }

    private fun backToHome() {
        binding.ivHomePantryBack.setOnClickListener {
            findNavController().navigate(R.id.action_home_pantry_to_home)
        }
    }
    private fun clickPlusFood(){
        binding.ivHomePantryPlusFood.setOnClickListener {
            val addFoodPopUp = AddFoodPopUp()
            addFoodPopUp.setStyle(
                BottomSheetDialogFragment.STYLE_NO_TITLE,
                R.style.Theme_Plantry_AlertDialog
            )
            addFoodPopUp.show(parentFragmentManager, FragmentAddFood.POP_UP)
        }
    }
    private fun setRcvList() {
        val adapter = PantryDayAdapter()
        binding.rcvHomePantryItemFoodList.adapter = adapter
        val pantryList_food = listOf(
            ResponseHomePantryDto.Result.Food(-1, -1, "👋", true, true, "food1"),
            ResponseHomePantryDto.Result.Food(2, 0, "🍉", false, true, "food4"),
            ResponseHomePantryDto.Result.Food(2, 2, "🍉", false, true, "food2"),
            ResponseHomePantryDto.Result.Food(3, 3, "🍇", false, false, "food3"),
        )
        val pantryList = listOf(
            ResponseHomePantryDto.Result(-1, pantryList_food),
            ResponseHomePantryDto.Result(0, pantryList_food),
            ResponseHomePantryDto.Result(1, pantryList_food),
            ResponseHomePantryDto.Result(2, pantryList_food)
        )
        setEmptyViewVisibility(pantryList)
        adapter.submitList(pantryList)
    }

    private fun setEmptyViewVisibility(pantryList : List<ResponseHomePantryDto.Result>){
        if(pantryList.isNullOrEmpty()){ // api 통신 후 조건 수정
            binding.layoutHomePantryEmptyView.clHomePantryEmptyView.visibility =View.VISIBLE
            binding.rcvHomePantryItemFoodList.visibility =View.GONE
        }
        else{
            binding.layoutHomePantryEmptyView.clHomePantryEmptyView.visibility =View.GONE
            binding.rcvHomePantryItemFoodList.visibility =View.VISIBLE
        }
    }



}