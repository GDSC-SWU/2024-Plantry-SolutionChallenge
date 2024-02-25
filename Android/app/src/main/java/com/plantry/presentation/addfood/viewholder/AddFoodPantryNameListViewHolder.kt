package com.plantry.presentation.addfood.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.plantry.data.dto.response.pantry.ResponsePantryDto
import com.plantry.databinding.ItemAddFoodPantryNameBinding

class AddFoodPantryNameListViewHolder(private val binding: ItemAddFoodPantryNameBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val _item_binding: ItemAddFoodPantryNameBinding = binding
    val item_binding: ItemAddFoodPantryNameBinding = _item_binding

    fun bind(pantry: ResponsePantryDto.Result) {
        binding.tvAddFoodPantryName.text = pantry.title
    }

}