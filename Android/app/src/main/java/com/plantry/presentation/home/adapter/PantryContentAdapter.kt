package com.plantry.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.plantry.data.dto.ResponseHomePantryDto
import com.plantry.data.dto.ResponseHomePantryDto.Data.Result.Food
import com.plantry.databinding.ItemHomePantryBinding
import com.plantry.databinding.ItemHomePantryContentBinding
import com.plantry.databinding.ItemHomePantryDDayBinding
import com.plantry.presentation.home.viewholder.PantryContentViewHolder
import com.plantry.presentation.home.viewholder.PantryDayViewHolder
import com.plantry.presentation.home.viewholder.PantryViewHolder

class PantryContentAdapter :
    ListAdapter<ResponseHomePantryDto.Data.Result.Food, PantryContentViewHolder>(PantryContentDiffCallback()) {

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

class PantryContentDiffCallback : DiffUtil.ItemCallback<ResponseHomePantryDto.Data.Result.Food>() {
    override fun areItemsTheSame(oldItem: ResponseHomePantryDto.Data.Result.Food, newItem: ResponseHomePantryDto.Data.Result.Food): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: ResponseHomePantryDto.Data.Result.Food, newItem: ResponseHomePantryDto.Data.Result.Food): Boolean {
        return oldItem == newItem
    }
}