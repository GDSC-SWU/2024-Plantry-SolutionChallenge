package com.plantry.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.plantry.data.dto.ResponseHomeDto
import com.plantry.databinding.ItemHomePantryBinding
import com.plantry.presentation.home.viewholder.PantryViewHolder

class PantryAdapter :
    ListAdapter<ResponseHomeDto, PantryViewHolder>(PantryDiffCallback()) {

    // 아이템 클릭되었는지 확인하기 위한 interface
    interface PantryItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: PantryItemClick? = null

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
        if (itemClick != null) {
            // else 버튼 클릭
            holder.item_binding.ivHomeItemElse.setOnClickListener { view ->
                itemClick?.onClick(view, position)
            }

            // item 클릭
            holder.itemView.setOnClickListener { view ->
                itemClick?.onClick(view, position)
            }

            // heart 클릭
            holder.item_binding.ivHomeItemHeart.setOnClickListener { view ->
                itemClick?.onClick(view, position)
            }
        }
    }
}

class PantryDiffCallback : DiffUtil.ItemCallback<ResponseHomeDto>() {
    override fun areItemsTheSame(oldItem: ResponseHomeDto, newItem: ResponseHomeDto): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: ResponseHomeDto, newItem: ResponseHomeDto): Boolean {
        return oldItem == newItem
    }
}