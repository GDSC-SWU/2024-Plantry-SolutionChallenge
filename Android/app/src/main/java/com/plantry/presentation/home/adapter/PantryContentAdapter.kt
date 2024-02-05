package com.plantry.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.plantry.data.dto.response.ResponseHomePantryDto
import com.plantry.databinding.ItemHomePantryContentBinding
import com.plantry.presentation.home.viewholder.PantryContentViewHolder

class PantryContentAdapter :
    ListAdapter<ResponseHomePantryDto.Result.Food, PantryContentViewHolder>(PantryContentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PantryContentViewHolder {
        val binding = ItemHomePantryContentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PantryContentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PantryContentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PantryContentDiffCallback : DiffUtil.ItemCallback<ResponseHomePantryDto.Result.Food>() {
    override fun areItemsTheSame(oldItem: ResponseHomePantryDto.Result.Food, newItem: ResponseHomePantryDto.Result.Food): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: ResponseHomePantryDto.Result.Food, newItem: ResponseHomePantryDto.Result.Food): Boolean {
        return oldItem == newItem
    }
}