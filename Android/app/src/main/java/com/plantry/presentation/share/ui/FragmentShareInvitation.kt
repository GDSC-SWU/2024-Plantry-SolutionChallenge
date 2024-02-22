package com.plantry.presentation.share.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.plantry.R
import com.plantry.coreui.adapter.ItemClick
import com.plantry.coreui.base.BindingFragment
import com.plantry.coreui.fragment.toast
import com.plantry.coreui.view.UiState
import com.plantry.data.dto.response.share.ResponseShareMemberDto
import com.plantry.databinding.FragmentShareCodeInvitationBinding
import com.plantry.presentation.share.adapter.ShareMemberAdapter
import com.plantry.presentation.share.popup.DeleteMemberPopUp
import com.plantry.presentation.share.viewmodel.ShareCodeResearchViewModel
import com.plantry.presentation.share.viewmodel.ShareCodeSearchViewModel
import com.plantry.presentation.share.viewmodel.ShareMemberViewModel

class FragmentShareInvitation :
    BindingFragment<FragmentShareCodeInvitationBinding>(R.layout.fragment_share_code_invitation) {
    override var bnvVisibility = View.GONE

    private val viewModelShareMemberList by viewModels<ShareMemberViewModel>({ requireParentFragment() })
    private val viewModelShareCodeSearch by viewModels<ShareCodeSearchViewModel>()
    private val viewModelShareCodeReSearch by viewModels<ShareCodeResearchViewModel>()

    override fun initView() {
        observeMemberList()

        setCode()
        observeCode()

        clickCopy()

        clickRefreshCode()
        observeRefreshCode()


    }

    private fun clickCopy() {
        binding.layoutHomeShareCodeInvitationCodeView.tvShareOwnerItemCopy.setOnClickListener {
            val clipboardManager =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val clipData = ClipData.newPlainText(
                "invitation code",
                binding.layoutHomeShareCodeInvitationCodeView.tvShareOwnerItemCode.text.toString()
            )
            clipboardManager.setPrimaryClip(clipData)
            toast("Text copied to clipboard")
        }
    }

    private fun setCode() {
        val pantry_id = arguments?.getInt("pantry_id")
        pantry_id?.let { viewModelShareCodeSearch.getShareCodeSearch(it) }
    }

    private fun clickRefreshCode() {
        val pantry_id = arguments?.getInt("pantry_id")
        binding.layoutHomeShareCodeInvitationCodeView.ivShareCodeRefresh.setOnClickListener {
            pantry_id?.let { it1 -> viewModelShareCodeReSearch.patchShareCodeResearch(it1) }
        }
    }

    private fun clickItem(adapter: ShareMemberAdapter, list: List<ResponseShareMemberDto.User>) {
        adapter.shareMemberItemClick = object : ItemClick {
            override fun onClick(view: View, position: Int) {
                when (view.id) {
                    R.id.iv_share_code_member_delete -> {
                        val memberDeletePopup = DeleteMemberPopUp()
                        memberDeletePopup.setStyle(
                            DialogFragment.STYLE_NO_TITLE, R.style.Theme_Plantry_AlertDialog
                        )
                        memberDeletePopup.arguments = Bundle().apply {
                            list[position].userId?.let { putInt("member_id", it) }
                        }
                        memberDeletePopup.show(parentFragmentManager, POP_UP_MEMEBER_DELETE)
                    }
                }
            }
        }
    }

    private fun observeMemberList() {
        viewModelShareMemberList.shareMember.observe(this) {
            when (it) {
                is UiState.Success -> {
                    val isOwner = it.data.isUserOwner
                    if (isOwner != null) {
                        checkOwnerAndSetVisibilty(isOwner)
                        val adapter = ShareMemberAdapter(requireContext(), isOwner)

                        binding.rcvHomeShareCodeItemMemberList.adapter = adapter
                        val pantryList = it.data.list
                        adapter.submitList(pantryList)
                        it.data.list?.let { list -> clickItem(adapter, list) }
                    }

                    if(it.data.list != null){
                        binding.tvHomeShareCodeMemberTitle.text = "Pantry Member(${it.data.list.size})"
                    }
                }

                else -> Unit
            }
        }
    }

    private fun observeRefreshCode() {
        viewModelShareCodeReSearch.shareCode.observe(this) {
            when (it) {
                is UiState.Success -> {
                    binding.layoutHomeShareCodeInvitationCodeView.tvShareOwnerItemCode.text =
                        it.data
                }

                else -> Unit
            }
        }
    }


    private fun observeCode() {
        viewModelShareCodeSearch.shareCode.observe(this) {
            when (it) {
                is UiState.Success -> {
                    binding.layoutHomeShareCodeInvitationCodeView.tvShareOwnerItemCode.text = it.data
                }

                else -> Unit
            }
        }
    }

    private fun checkOwnerAndSetVisibilty(isUserOwner: Boolean) {
        if (isUserOwner) {
            binding.tvHomeShareCodeOwnerInvitationTitle.visibility = View.VISIBLE
            binding.layoutHomeShareCodeInvitationCodeView.clShareOwnerCodeShow.visibility =
                View.VISIBLE
        } else {
            binding.tvHomeShareCodeOwnerInvitationTitle.visibility = View.GONE
            binding.layoutHomeShareCodeInvitationCodeView.clShareOwnerCodeShow.visibility =
                View.GONE
        }
    }

    companion object {
        const val POP_UP_MEMEBER_DELETE = "share_invitation_to_delete_member"
    }
}