package com.plantry.presentation.addfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.plantry.data.dto.response.product.ResponseProductIconListDto
import com.plantry.databinding.ItemAddFoodIconBinding
import com.plantry.presentation.addfood.viewholder.AddFoodIconViewHolder

class FoodIconListAdapter (private val itemClickListener: OnFoodItemClickListener):
    ListAdapter<ResponseProductIconListDto.Food, AddFoodIconViewHolder>(ProductIConDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFoodIconViewHolder {
        val binding = ItemAddFoodIconBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AddFoodIconViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddFoodIconViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(getItem(position))
        }
    }
}

class ProductIConDiffCallback : DiffUtil.ItemCallback<ResponseProductIconListDto.Food>() {
    override fun areItemsTheSame(oldItem: ResponseProductIconListDto.Food, newItem: ResponseProductIconListDto.Food): Boolean {
        return oldItem.name == newItem.name

    }

    override fun areContentsTheSame(oldItem: ResponseProductIconListDto.Food, newItem: ResponseProductIconListDto.Food): Boolean {
        return oldItem == newItem
    }
}

interface OnFoodItemClickListener {
    fun onItemClick(item: ResponseProductIconListDto.Food)
}
