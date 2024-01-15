package com.plantry.presentation.home.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.plantry.R
import com.plantry.data.dto.ResponseHomeDto
import com.plantry.databinding.ItemHomePantryBinding

class PantryViewHolder(private val binding: ItemHomePantryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val _item_binding:ItemHomePantryBinding = binding
    val item_binding: ItemHomePantryBinding = _item_binding

    fun bind(pantry: ResponseHomeDto) {
        if (pantry.name.isNullOrEmpty()) {
            setVisibilityChange()
        } else {
            _item_binding.ivHomeItemBackground.setBackgroundResource(checkBackgroundColor(pantry.color))
            _item_binding.tvHomeItemPantryName.text = pantry.name
            _item_binding.ivHomeItemHeart.isSelected = pantry.heart
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
            "r" -> {
                return R.drawable.shape_fill_cover_red_16_rec_top
            }

            "o" -> {
                return R.drawable.shape_fill_cover_orange_16_rec_top
            }

            "y" -> {
                return R.drawable.shape_fill_cover_yellow_16_rec_top
            }

            "g" -> {
                return R.drawable.shape_fill_cover_green_16_rec_top
            }

            "b" -> {
                return R.drawable.shape_fill_cover_blue_16_rec_top
            }

            "p" -> {
                return R.drawable.shape_fill_cover_purple_16_rec_top
            }
        }

        // 지정 안 했을 때의 기본 색상
        return R.drawable.shape_fill_cover_red_16_rec_top
    }
}
