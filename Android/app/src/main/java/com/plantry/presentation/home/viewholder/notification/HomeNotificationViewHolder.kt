package com.plantry.presentation.home.viewholder.notification

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.plantry.R
import com.plantry.data.dto.response.notification.ResponseNortificationAllListDto
import com.plantry.data.dto.response.profile.ResponseProfileMissionListDto
import com.plantry.databinding.ItemNotificationBinding
import com.plantry.databinding.ItemProfileMissionBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeNotificationViewHolder(private val binding: ItemNotificationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val _itemBinding: ItemNotificationBinding = binding
    val itemBinding: ItemNotificationBinding = _itemBinding

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(noti: ResponseNortificationAllListDto.Result) {
        binding.tvNotificationBody.text = noti.body
        binding.tvNotificationTime.text = noti.notifiedAt?.let { calculateTimeDifference(it) }
        binding.tvNotificationTitle.text = noti.title
        binding.clNotificationItemLayout.isSelected = noti.isChecked ?: true
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateTimeDifference(dateTimeString: String): String {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val targetDateTime = LocalDateTime.parse(dateTimeString, formatter)
        val currentDateTime = LocalDateTime.now()

        val daysDifference = currentDateTime.toLocalDate().toEpochDay() - targetDateTime.toLocalDate().toEpochDay()

        return when (daysDifference) {
            0L -> "Just now"
            in 1..7L -> "$daysDifference days ago"
            else -> targetDateTime.format(DateTimeFormatter.ofPattern("YYYY.MM.DD"))
        }
    }
}