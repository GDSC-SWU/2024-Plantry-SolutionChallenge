package com.plantry.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.plantry.data.dto.ResponseHomePantryDto
import com.plantry.databinding.ItemHomePantryDDayBinding
import com.plantry.presentation.home.viewholder.PantryDayViewHolder

class PantryDayAdapter :
    ListAdapter<ResponseHomePantryDto.Data.Result, PantryDayViewHolder>(PantryDayDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PantryDayViewHolder {
        val binding = ItemHomePantryDDayBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PantryDayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PantryDayViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PantryDayDiffCallback : DiffUtil.ItemCallback<ResponseHomePantryDto.Data.Result>() {
    override fun areItemsTheSame(
        oldItem: ResponseHomePantryDto.Data.Result,
        newItem: ResponseHomePantryDto.Data.Result
    ): Boolean {
        return oldItem.day == newItem.day
    }

    override fun areContentsTheSame(
        oldItem: ResponseHomePantryDto.Data.Result,
        newItem: ResponseHomePantryDto.Data.Result
    ): Boolean {
        return oldItem == newItem
    }
}