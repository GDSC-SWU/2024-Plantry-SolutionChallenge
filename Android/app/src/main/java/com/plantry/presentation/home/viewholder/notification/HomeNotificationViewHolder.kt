package com.plantry.presentation.home.viewholder.notification

import androidx.recyclerview.widget.RecyclerView
import com.plantry.R
import com.plantry.data.dto.response.notification.ResponseNortificationAllListDto
import com.plantry.data.dto.response.profile.ResponseProfileMissionListDto
import com.plantry.databinding.ItemNotificationBinding
import com.plantry.databinding.ItemProfileMissionBinding

class HomeNotificationViewHolder(private val binding: ItemNotificationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val _itemBinding: ItemNotificationBinding = binding
    val itemBinding: ItemNotificationBinding = _itemBinding

    fun bind(noti: ResponseNortificationAllListDto.Result) {
        binding.tvNotificationBody.text = noti.body
        binding.tvNotificationTime.text = noti.notifiedAt
        binding.tvNotificationTitle.text = noti.title
        binding.clNotificationItemLayout.isSelected = noti.isChecked ?: true
    }
}