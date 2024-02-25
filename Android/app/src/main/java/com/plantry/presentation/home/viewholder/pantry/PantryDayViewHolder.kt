package com.plantry.presentation.home.viewholder.pantry

import androidx.recyclerview.widget.RecyclerView
import com.plantry.R
import com.plantry.data.dto.response.product.ResponseProductListDto
import com.plantry.databinding.ItemHomePantryDDayBinding
import com.plantry.presentation.home.adapter.pantry.OnItemClickListener
import com.plantry.presentation.home.adapter.pantry.PantryContentAdapter

class PantryDayViewHolder(private val binding: ItemHomePantryDDayBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(pantry_day: ResponseProductListDto.Result, itemClickListener: OnItemClickListener) {
        setColorAndDDay(pantry_day.day, pantry_day.list?.size)
        setContentRcvList(pantry_day.list, itemClickListener)
    }

    private fun setColorAndDDay(day: Int?, count: Int?) {
        when (day) {
            -1 -> {
                binding.ivHomePantryItemDDayColor.setBackgroundResource(R.drawable.shape_fill_highlight_redday_oval)
                binding.tvHomePantryItemDDayDay.text = "Expired (${count})"
            }

            0 -> {
                binding.ivHomePantryItemDDayColor.setBackgroundResource(R.drawable.shape_fill_highlight_orangeday_oval)
                binding.tvHomePantryItemDDayDay.text = "D-day (${count})"
            }

            else -> {
                binding.ivHomePantryItemDDayColor.setBackgroundResource(R.drawable.shape_fill_primary_color_oval)
                binding.tvHomePantryItemDDayDay.text = "D-${day} (${count})"
            }
        }
    }

    private fun setContentRcvList(list: List<ResponseProductListDto.Result.Food>?, itemClickListener: OnItemClickListener) {
        val adapter = PantryContentAdapter(itemClickListener)
        binding.rcvHomePantryItemDDayContentList.adapter = adapter
        adapter.submitList(list)
    }

}
