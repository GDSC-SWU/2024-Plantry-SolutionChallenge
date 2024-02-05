package com.plantry.presentation.home.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.plantry.R
import com.plantry.data.dto.response.ResponseHomeDto
import com.plantry.databinding.ItemHomePantryBinding

class PantryViewHolder(private val binding: ItemHomePantryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val _item_binding:ItemHomePantryBinding = binding
    val item_binding: ItemHomePantryBinding = _item_binding

    fun bind(pantry: ResponseHomeDto.Result) {
        if (pantry.title.isNullOrEmpty()) {
            setVisibilityChange()
        } else {
            _item_binding.ivHomeItemBackground.setBackgroundResource(checkBackgroundColor(pantry.color))
            _item_binding.tvHomeItemPantryName.text = pantry.title
            _item_binding.ivHomeItemHeart.isSelected = pantry.isMarked
        }
    }

    private fun setVisibilityChange() {
        // 기존 Pantry 아이템
        _item_binding.ivHomeItemBackground.visibility = View.INVISIBLE
        _item_binding.ivHomeItemHeart.visibility = View.INVISIBLE
        _item_binding.tvHomeItemPantryName.visibility = View.INVISIBLE
        _item_binding.ivHomeItemElse.visibility = View.INVISIBLE

        // Add Pantry 아이템
        _item_binding.tvHomeItemPlus.visibility = View.VISIBLE
        _item_binding.ivHomeItemPlus.visibility = View.VISIBLE
    }

    private fun checkBackgroundColor(color: String): Int {
        when (color) {
            "FFA5A0" -> {
                return R.drawable.shape_fill_cover_red_16_rec_top
            }

            "FFCE8A" -> {
                return R.drawable.shape_fill_cover_orange_16_rec_top
            }

            "FFE88A" -> {
                return R.drawable.shape_fill_cover_yellow_16_rec_top
            }

            "A2E5B3" -> {
                return R.drawable.shape_fill_cover_green_16_rec_top
            }

            "8AC2FF" -> {
                return R.drawable.shape_fill_cover_blue_16_rec_top
            }

            "DAAFF0" -> {
                return R.drawable.shape_fill_cover_purple_16_rec_top
            }
        }

        // 지정 안 했을 때의 기본 색상
        return R.drawable.shape_fill_cover_red_16_rec_top
    }
}
