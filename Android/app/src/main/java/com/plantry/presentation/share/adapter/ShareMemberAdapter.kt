package com.plantry.presentation.share.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.plantry.coreui.adapter.ItemClick
import com.plantry.data.dto.response.share.ResponseShareMemberDto
import com.plantry.databinding.ItemShareCodeMemberBinding
import com.plantry.presentation.share.viewholder.ShareMemberViewHolder

class ShareMemberAdapter (val context: Context, val isOwner:Boolean):
    ListAdapter<ResponseShareMemberDto.User, ShareMemberViewHolder>(ShareMemberDiffCallback()) {

    var shareMemberItemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareMemberViewHolder {
        val binding = ItemShareCodeMemberBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShareMemberViewHolder(binding, context = context, isOwner= isOwner)
    }

    override fun onBindViewHolder(holder: ShareMemberViewHolder, position: Int) {
        holder.bind(getItem(position))
        if (shareMemberItemClick != null) {
            // else 버튼 클릭
            holder.item_binding.ivShareCodeMemberDelete.setOnClickListener { view ->
                shareMemberItemClick?.onClick(view, position)
            }

        }
    }
}

class ShareMemberDiffCallback : DiffUtil.ItemCallback<ResponseShareMemberDto.User>() {
    override fun areItemsTheSame(oldItem: ResponseShareMemberDto.User, newItem: ResponseShareMemberDto.User): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(oldItem: ResponseShareMemberDto.User, newItem: ResponseShareMemberDto.User): Boolean {
        return oldItem == newItem
    }
}