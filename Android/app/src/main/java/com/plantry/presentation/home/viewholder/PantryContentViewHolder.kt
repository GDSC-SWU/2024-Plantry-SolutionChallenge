package com.plantry.presentation.home.viewholder

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.plantry.MainActivity
import com.plantry.R
import com.plantry.data.dto.ResponseHomePantryDto
import com.plantry.databinding.ItemHomePantryContentBinding
import com.plantry.presentation.home.bottomsheet.HomeAlarmBottomSheet
import com.plantry.presentation.home.ui.FragmentHome
import com.plantry.presentation.home.ui.FragmentHomePantry
import kotlin.math.roundToInt

class PantryContentViewHolder(private val binding: ItemHomePantryContentBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(pantry_content: ResponseHomePantryDto.Data.Result.Food) {
        binding.tvHomePantryItemContentImg.setText(pantry_content.icon)
        binding.tvHomePantryItemContentFoodName.setText(pantry_content.name)
        setCount(pantry_content.count)
        setDDay(pantry_content.days)
        setDateSort(pantry_content.isUseBydate)
        setAlarmIcon(true)
        clickAlarm()
    }

    private fun setCount(count: Double?) {
        if (count != null) {
            if (count < count.roundToInt()) {
                binding.tvHomePantryItemContentCount.text = count.toString()
            } else {
                binding.tvHomePantryItemContentCount.text = count.roundToInt().toString()
            }
        }
    }

    private fun setDDay(day: Int?) {
        if (day != null) {
            if (day < 0) {
                binding.tvHomePantryItemContentDDay.text = "D+${-(day)}"
            } else if (day == 0) {
                binding.tvHomePantryItemContentDDay.text = "D-day"
            } else {
                binding.tvHomePantryItemContentDDay.text = "D-${(day)}"
            }
        }
    }

    private fun setDateSort(isUseBydate: Boolean?) {
        if (isUseBydate == true) {
            binding.tvHomePantryItemContentDate.text = "Use-by Date"
            binding.tvHomePantryItemContentDate.setTextColor(Color.parseColor("#FF6259"))
        } else {
            binding.tvHomePantryItemContentDate.text = "Sell-by Date"
            binding.tvHomePantryItemContentDate.setTextColor(Color.parseColor("#FF9500"))
        }
    }

    private fun clickAlarm() {
        val homePantryFragment = FragmentHomePantry()
        binding.ivHomePantryItemContentAlarm.setOnClickListener {
            val alarmBottomSheet = HomeAlarmBottomSheet()
            alarmBottomSheet.show(homePantryFragment.childFragmentManager, FragmentHome.BOTTOM_SHEET)
        }
    }

    private fun setAlarmIcon(isAlarmOn: Boolean) {
        if(isAlarmOn){ // 데이터 구조 변화에 따라 변경 예정
            binding.ivHomePantryItemContentAlarm.setBackgroundResource(R.drawable.ic_home_pantry_alarm_on)
        }
        else{
            binding.ivHomePantryItemContentAlarm.setBackgroundResource(R.drawable.ic_home_pantry_alarm_off)
        }
    }
}
