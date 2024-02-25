package com.plantry.presentation.home.adapter.notification

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.plantry.coreui.adapter.ItemClick
import com.plantry.data.dto.response.notification.ResponseNortificationAllListDto
import com.plantry.data.dto.response.profile.ResponseProfileAlarmChangeDto
import com.plantry.data.dto.response.profile.ResponseProfileMissionListDto
import com.plantry.databinding.ItemNotificationBinding
import com.plantry.databinding.ItemProfileMissionBinding
import com.plantry.presentation.profile.adapter.ProfileMisstionDiffCallback
import com.plantry.presentation.home.viewholder.notification.HomeNotificationViewHolder

class HomeNotificationAdapter:
    ListAdapter<ResponseNortificationAllListDto.Result, HomeNotificationViewHolder>(
        HomeNotificationDiffCallback()
    )  {


    var notificationClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeNotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HomeNotificationViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HomeNotificationViewHolder, position: Int) {
            holder.bind(getItem(position))
        if (notificationClick != null) {
            holder.itemView.setOnClickListener {view ->
                notificationClick?.onClick(view, position)
            }
        }
    }
}

class HomeNotificationDiffCallback : DiffUtil.ItemCallback<ResponseNortificationAllListDto.Result>() {
    override fun areItemsTheSame(oldItem: ResponseNortificationAllListDto.Result, newItem: ResponseNortificationAllListDto.Result): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ResponseNortificationAllListDto.Result, newItem: ResponseNortificationAllListDto.Result): Boolean {
        return oldItem == newItem
    }
}