package com.plantry.presentation.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.plantry.coreui.adapter.ItemClick
import com.plantry.data.dto.response.profile.ResponseProfileMissionListDto
import com.plantry.databinding.ItemProfileMissionBinding
import com.plantry.presentation.profile.viewholder.ProfileMissionViewHolder

class ProfileMissionViewPagerAdapter:
    ListAdapter<ResponseProfileMissionListDto.Result, ProfileMissionViewHolder>(ProfileMisstionDiffCallback())  {


    var missionNameClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileMissionViewHolder {
        val binding = ItemProfileMissionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProfileMissionViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ProfileMissionViewHolder, position: Int) {
            holder.bind(getItem(position))
        if (missionNameClick != null) {
            holder.item_binding.tvProfileMissionItemName.setOnClickListener {view ->
                missionNameClick?.onClick(view, position)
            }
        }
    }
}


class ProfileMisstionDiffCallback : DiffUtil.ItemCallback<ResponseProfileMissionListDto.Result>() {
    override fun areItemsTheSame(oldItem: ResponseProfileMissionListDto.Result, newItem: ResponseProfileMissionListDto.Result): Boolean {
        return oldItem.missionId == newItem.missionId
    }

    override fun areContentsTheSame(oldItem: ResponseProfileMissionListDto.Result, newItem: ResponseProfileMissionListDto.Result): Boolean {
        return oldItem == newItem
    }
}