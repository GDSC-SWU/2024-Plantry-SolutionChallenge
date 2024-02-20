package com.plantry.presentation.home.viewholder

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.plantry.R
import com.plantry.data.dto.response.product.ResponseProductListDto
import com.plantry.databinding.ItemHomePantryContentBinding
import kotlin.math.roundToInt

class PantryContentViewHolder(private val binding: ItemHomePantryContentBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val _item_binding: ItemHomePantryContentBinding = binding
    val item_binding: ItemHomePantryContentBinding = _item_binding

    fun bind(pantry_content: ResponseProductListDto.Result.Food) {
        binding.tvHomePantryItemContentImg.setText(pantry_content.icon)
        binding.tvHomePantryItemContentFoodName.setText(pantry_content.name)
        binding.tvHomePantryItemContentCount.text = pantry_content.count.toString()
        setCount(pantry_content.count)
        setDDay(pantry_content.days)
        setDateSort(pantry_content.isUseBydate)
        setAlarmIcon(pantry_content.isNotified)
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

    private fun setAlarmIcon(isAlarmOn: Boolean?) {
        if (isAlarmOn == true) { // 데이터 구조 변화에 따라 변경 예정
            binding.ivHomePantryItemContentAlarm.setBackgroundResource(R.drawable.ic_home_pantry_alarm_on)
        } else if (isAlarmOn == false) {
            binding.ivHomePantryItemContentAlarm.setBackgroundResource(R.drawable.ic_home_pantry_alarm_off)
        } else {

        }
    }
}
