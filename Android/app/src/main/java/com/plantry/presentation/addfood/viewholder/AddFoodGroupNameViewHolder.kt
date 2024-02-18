package com.plantry.presentation.addfood.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.plantry.data.dto.response.product.ResponseProductIconListDto
import com.plantry.databinding.ItemAddFoodTitleBinding
import com.plantry.presentation.addfood.adapter.FoodIconListAdapter
import com.plantry.presentation.addfood.adapter.OnFoodItemClickListener

class AddFoodGroupNameViewHolder(private val binding: ItemAddFoodTitleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(productGroup: ResponseProductIconListDto, itemClickListener: OnFoodItemClickListener) {
        binding.tvAddFoodIconSelectTitle.text= productGroup.groupName
        setContentRcvList(productGroup.foodList, itemClickListener)
    }

    private fun setContentRcvList(list: List<ResponseProductIconListDto.Food>, itemClickListener: OnFoodItemClickListener) {
        val adapter = FoodIconListAdapter(itemClickListener)
        binding.rcvAddFood.adapter = adapter
        adapter.submitList(list)
    }

}