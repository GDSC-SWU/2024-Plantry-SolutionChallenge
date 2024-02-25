package com.plantry.presentation.addfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.plantry.coreui.adapter.ItemClick
import com.plantry.data.dto.response.pantry.ResponsePantryDto
import com.plantry.databinding.ItemAddFoodPantryNameBinding
import com.plantry.presentation.addfood.viewholder.AddFoodPantryNameListViewHolder

class PantryNameListAdapter :
    ListAdapter<ResponsePantryDto.Result, AddFoodPantryNameListViewHolder>(PantryDiffCallback()) {


    var nameClick: ItemClick? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddFoodPantryNameListViewHolder {
        val binding = ItemAddFoodPantryNameBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AddFoodPantryNameListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddFoodPantryNameListViewHolder, position: Int) {
        holder.bind(getItem(position))
        if (nameClick != null) {
            holder.itemView.setOnClickListener {view->
                nameClick?.onClick(view, position)
            }
        }
    }
}

class PantryDiffCallback : DiffUtil.ItemCallback<ResponsePantryDto.Result>() {
    override fun areItemsTheSame(
        oldItem: ResponsePantryDto.Result,
        newItem: ResponsePantryDto.Result
    ): Boolean {
        return oldItem.id == newItem.id

    }

    override fun areContentsTheSame(
        oldItem: ResponsePantryDto.Result,
        newItem: ResponsePantryDto.Result
    ): Boolean {
        return oldItem == newItem
    }
}