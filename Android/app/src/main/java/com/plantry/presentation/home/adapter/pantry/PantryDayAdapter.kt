package com.plantry.presentation.home.adapter.pantry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.plantry.data.dto.response.product.ResponseProductListDto
import com.plantry.databinding.ItemHomePantryDDayBinding
import com.plantry.presentation.home.viewholder.pantry.PantryDayViewHolder

class PantryDayAdapter(private val itemClickListener: OnItemClickListener) :
    ListAdapter<ResponseProductListDto.Result, PantryDayViewHolder>(PantryDayDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PantryDayViewHolder {
        val binding = ItemHomePantryDDayBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PantryDayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PantryDayViewHolder, position: Int) {
        holder.bind(getItem(position), itemClickListener)
    }
}

class PantryDayDiffCallback : DiffUtil.ItemCallback<ResponseProductListDto.Result>() {
    override fun areItemsTheSame(
        oldItem: ResponseProductListDto.Result,
        newItem: ResponseProductListDto.Result
    ): Boolean {
        return oldItem.day == newItem.day
    }

    override fun areContentsTheSame(
        oldItem: ResponseProductListDto.Result,
        newItem: ResponseProductListDto.Result
    ): Boolean {
        return oldItem == newItem
    }
}