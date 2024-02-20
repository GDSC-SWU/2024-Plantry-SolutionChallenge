package com.plantry.presentation.home.adapter.pantry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.plantry.coreui.adapter.ItemClick
import com.plantry.data.dto.response.pantry.ResponsePantryDto
import com.plantry.databinding.ItemHomePantryBinding
import com.plantry.presentation.home.viewholder.pantry.PantryViewHolder

class PantryAdapter :
    ListAdapter<ResponsePantryDto.Result, PantryViewHolder>(PantryDiffCallback()) {

    var pantryItemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PantryViewHolder {
        val binding = ItemHomePantryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PantryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PantryViewHolder, position: Int) {
        holder.bind(getItem(position))
        if (pantryItemClick != null) {
            // else 버튼 클릭
            holder.item_binding.ivHomeItemElse.setOnClickListener { view ->
                pantryItemClick?.onClick(view, position)
            }

            // item 클릭
            holder.itemView.setOnClickListener { view ->
                pantryItemClick?.onClick(view, position)
            }

            // heart 클릭
            holder.item_binding.ivHomeItemHeart.setOnClickListener { view ->
                pantryItemClick?.onClick(view, position)
            }
        }
    }
}

class PantryDiffCallback : DiffUtil.ItemCallback<ResponsePantryDto.Result>() {
    override fun areItemsTheSame(oldItem: ResponsePantryDto.Result, newItem: ResponsePantryDto.Result): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ResponsePantryDto.Result, newItem: ResponsePantryDto.Result): Boolean {
        return oldItem == newItem
    }
}