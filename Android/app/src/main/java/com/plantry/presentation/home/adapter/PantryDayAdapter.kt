package com.plantry.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.plantry.data.dto.response.ResponseHomePantryDto
import com.plantry.databinding.ItemHomePantryDDayBinding
import com.plantry.presentation.home.viewholder.PantryDayViewHolder

class PantryDayAdapter :
    ListAdapter<ResponseHomePantryDto.Result, PantryDayViewHolder>(PantryDayDiffCallback()) {

    var itemClick: PantryAdapter.PantryItemClick? = null

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

class PantryDayDiffCallback : DiffUtil.ItemCallback<ResponseHomePantryDto.Result>() {
    override fun areItemsTheSame(
        oldItem: ResponseHomePantryDto.Result,
        newItem: ResponseHomePantryDto.Result
    ): Boolean {
        return oldItem.day == newItem.day
    }

    override fun areContentsTheSame(
        oldItem: ResponseHomePantryDto.Result,
        newItem: ResponseHomePantryDto.Result
    ): Boolean {
        return oldItem == newItem
    }
}