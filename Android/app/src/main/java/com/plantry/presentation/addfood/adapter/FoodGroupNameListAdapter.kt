package com.plantry.presentation.addfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.plantry.data.dto.response.product.ResponseProductIconListDto
import com.plantry.databinding.ItemAddFoodTitleBinding
import com.plantry.presentation.addfood.viewholder.AddFoodGroupNameViewHolder
import com.plantry.presentation.home.adapter.OnItemClickListener

class FoodGroupNameListAdapter(private val itemClickListener: OnFoodItemClickListener) :
    ListAdapter<ResponseProductIconListDto, AddFoodGroupNameViewHolder>(ProductGroupNameDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFoodGroupNameViewHolder {
        val binding = ItemAddFoodTitleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AddFoodGroupNameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddFoodGroupNameViewHolder, position: Int) {
        holder.bind(getItem(position), itemClickListener)
    }
}

class ProductGroupNameDiffCallback : DiffUtil.ItemCallback<ResponseProductIconListDto>() {
    override fun areItemsTheSame(
        oldItem: ResponseProductIconListDto,
        newItem: ResponseProductIconListDto
    ): Boolean {
        return oldItem.groupName == newItem.groupName

    }

    override fun areContentsTheSame(
        oldItem: ResponseProductIconListDto,
        newItem: ResponseProductIconListDto
    ): Boolean {
        return oldItem == newItem
    }
}