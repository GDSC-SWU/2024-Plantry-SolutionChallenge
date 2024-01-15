package com.plantry.presentation.home.ui

import android.view.View
import androidx.navigation.fragment.findNavController
import com.plantry.R
import com.plantry.coreui.base.BindingFragment
import com.plantry.data.dto.ResponseHomePantryDto
import com.plantry.databinding.FragmentHomePantryBinding
import com.plantry.presentation.home.adapter.PantryDayAdapter


class FragmentHomePantry :
    BindingFragment<FragmentHomePantryBinding>(R.layout.fragment_home_pantry) {

    override var bnvVisibility = View.GONE

    override fun initView() {
        setPantryName()
        backToHome()
        setRcvList()
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

    private fun setRcvList() {
        val adapter = PantryDayAdapter()
        binding.rcvHomePantryItemFoodList.adapter = adapter
        val pantryList_food = listOf(
            ResponseHomePantryDto.Data.Result.Food(-1.0, -1, "👋", true, "food1"),
            ResponseHomePantryDto.Data.Result.Food(2.5, 0, "🍉", false, "food4"),
            ResponseHomePantryDto.Data.Result.Food(2.0, 2, "🍉", false, "food2"),
            ResponseHomePantryDto.Data.Result.Food(3.0, 3, "🍇", false, "food3"),
        )
        val pantryList = listOf(
            ResponseHomePantryDto.Data.Result(-1, pantryList_food),
            ResponseHomePantryDto.Data.Result(0, pantryList_food),
            ResponseHomePantryDto.Data.Result(1, pantryList_food),
            ResponseHomePantryDto.Data.Result(2, pantryList_food)
        )
        setEmptyViewVisibility(pantryList)
        adapter.submitList(pantryList)
    }

    private fun setEmptyViewVisibility(pantryList : List<ResponseHomePantryDto.Data.Result>){
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