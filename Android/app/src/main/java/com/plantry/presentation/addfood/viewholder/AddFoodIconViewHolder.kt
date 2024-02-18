package com.plantry.presentation.addfood.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.plantry.data.dto.response.product.ResponseProductIconListDto
import com.plantry.databinding.ItemAddFoodIconBinding

class AddFoodIconViewHolder(private val binding: ItemAddFoodIconBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(foodInfo: ResponseProductIconListDto.Food) {
        binding.tvItemAddFoodIcon.text = foodInfo.icon
        binding.tvItemAddFoodName.text = foodInfo.name
    }

}