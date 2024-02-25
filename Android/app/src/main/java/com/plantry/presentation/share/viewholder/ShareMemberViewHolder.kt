package com.plantry.presentation.share.viewholder

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.plantry.data.dto.response.share.ResponseShareMemberDto
import com.plantry.databinding.ItemShareCodeMemberBinding

class ShareMemberViewHolder(private val binding: ItemShareCodeMemberBinding, val context: Context, val isOwner:Boolean) :
    RecyclerView.ViewHolder(binding.root) {

    private val _item_binding: ItemShareCodeMemberBinding = binding
    val item_binding: ItemShareCodeMemberBinding = _item_binding

    fun bind(member: ResponseShareMemberDto.User) {
        Log.d("aaa", member.toString())
        if(isOwner){
            if (member.isOwner == true) {
                binding.tvShareUserOwner.visibility = View.VISIBLE
                binding.ivShareCodeMemberDelete.visibility = View.GONE
            } else {
                binding.tvShareUserOwner.visibility = View.GONE
                binding.ivShareCodeMemberDelete.visibility = View.VISIBLE
            }
        }
        else{
            binding.tvShareUserOwner.visibility = View.GONE
            binding.ivShareCodeMemberDelete.visibility = View.GONE
        }

        binding.tvShareUserName.text = member.nickname
        binding.ivProfileProfileImg.load(member.profileImagePath) {
            transformations(CircleCropTransformation())
            size(dpToPx(context = context, dp = 52))
        }
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        )
        return px.toInt()
    }
}
