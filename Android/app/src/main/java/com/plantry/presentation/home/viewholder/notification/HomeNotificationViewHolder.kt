package com.plantry.presentation.home.viewholder.notification

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.plantry.R
import com.plantry.data.dto.response.notification.ResponseNortificationAllListDto
import com.plantry.data.dto.response.profile.ResponseProfileMissionListDto
import com.plantry.databinding.ItemNotificationBinding
import com.plantry.databinding.ItemProfileMissionBinding
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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
        val formatterIso = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val targetDateTime = LocalDateTime.parse(dateTimeString, formatterIso)
        val currentDate = LocalDate.now()

        val targetDate = formatDateString(targetDateTime.toString().substringBefore("T"))
        val formatterDate = DateTimeFormatter.ofPattern("yyyy.MM.dd")

        val daysDifference = currentDate.toEpochDay() - LocalDate.parse(targetDate, formatterDate).toEpochDay()

        return when {
            daysDifference == 0L -> "Just now"
            daysDifference in 1..7L -> "$daysDifference days ago"
            else -> targetDate
        }
    }

    fun formatDateString(inputDateString: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

        val date = LocalDate.parse(inputDateString, inputFormatter)
        return outputFormatter.format(date)
    }

}