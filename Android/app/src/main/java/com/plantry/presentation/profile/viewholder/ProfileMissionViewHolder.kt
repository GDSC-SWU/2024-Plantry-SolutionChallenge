package com.plantry.presentation.profile.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.plantry.data.dto.response.profile.ResponseProfileMissionListDto
import com.plantry.databinding.ItemProfileMissionBinding

class ProfileMissionViewHolder(private val binding: ItemProfileMissionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val _item_binding: ItemProfileMissionBinding = binding
    val item_binding: ItemProfileMissionBinding = _item_binding

    fun bind(mission: ResponseProfileMissionListDto.Result) {
        binding.tvProfileMissionItemName.text = mission.content
        if (mission.isAchieved != null) {
            binding.tvProfileMissionItemName.isSelected = mission.isAchieved
        }
    }
}