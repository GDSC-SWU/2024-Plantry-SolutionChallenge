package com.plantry.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.plantry.data.dto.response.product.ResponseProductListDto
import com.plantry.databinding.ItemHomePantryContentBinding
import com.plantry.presentation.home.viewholder.PantryContentViewHolder

class PantryContentAdapter (private val itemClickListener: OnItemClickListener) :
    ListAdapter<ResponseProductListDto.Result.Food, PantryContentViewHolder>(
        PantryContentDiffCallback()
    ) {
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
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(getItem(position))
        }
        holder.item_binding.ivHomePantryItemContentAlarm.setOnClickListener {
            itemClickListener.onAlarmClick(getItem(position))
        }
        holder.item_binding.ivHomePantryItemContentPlus.setOnClickListener {
            itemClickListener.onPlusClick(getItem(position))
        }
        holder.item_binding.ivHomePantryItemContentMinus.setOnClickListener {
            itemClickListener.onMinusClick(getItem(position))
        }
    }

}

class PantryContentDiffCallback : DiffUtil.ItemCallback<ResponseProductListDto.Result.Food>() {
    override fun areItemsTheSame(
        oldItem: ResponseProductListDto.Result.Food,
        newItem: ResponseProductListDto.Result.Food
    ): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: ResponseProductListDto.Result.Food,
        newItem: ResponseProductListDto.Result.Food
    ): Boolean {
        return oldItem == newItem
    }
}

interface OnItemClickListener {
    fun onItemClick(item: ResponseProductListDto.Result.Food)
    fun onAlarmClick(item: ResponseProductListDto.Result.Food)
    fun onPlusClick(item: ResponseProductListDto.Result.Food)
    fun onMinusClick(item: ResponseProductListDto.Result.Food)
}
