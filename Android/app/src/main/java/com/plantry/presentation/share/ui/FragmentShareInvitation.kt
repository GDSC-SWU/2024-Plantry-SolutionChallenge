package com.plantry.presentation.share.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.plantry.R
import com.plantry.coreui.adapter.ItemClick
import com.plantry.coreui.base.BindingFragment
import com.plantry.coreui.view.UiState
import com.plantry.data.dto.response.share.ResponseShareMemberDto
import com.plantry.databinding.FragmentShareCodeInvitationBinding
import com.plantry.presentation.share.adapter.ShareMemberAdapter
import com.plantry.presentation.share.popup.DeleteMemberPopUp
import com.plantry.presentation.share.viewmodel.ShareCodeResearchViewModel
import com.plantry.presentation.share.viewmodel.ShareCodeSearchViewModel
import com.plantry.presentation.share.viewmodel.ShareMemberSearchViewModel
import com.plantry.presentation.share.viewmodel.ShareMemberViewModel

class FragmentShareInvitation :
    BindingFragment<FragmentShareCodeInvitationBinding>(R.layout.fragment_share_code_invitation) {
    override var bnvVisibility = View.GONE

    private val viewModelShareMemberList by viewModels<ShareMemberViewModel>({ requireParentFragment() })
    private val viewModelShareCodeSearch by viewModels<ShareCodeSearchViewModel>()
    private val viewModelShareCodeReSearch by viewModels<ShareCodeResearchViewModel>()
    private val viewModelShareMemberSearch by viewModels<ShareMemberSearchViewModel>()

    override fun initView() {
        observeMemberList()

        setCode()
        observeCode()

        clickCopy()

        clickRefreshCode()
        observeRefreshCode()

        observeSearch()
        stopSearch()
        checkDateValidate()


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
                        val pantry_id = arguments?.getInt("pantry_id")
                        val memberDeletePopup = DeleteMemberPopUp()
                        memberDeletePopup.setStyle(
                            DialogFragment.STYLE_NO_TITLE, R.style.Theme_Plantry_AlertDialog
                        )
                        memberDeletePopup.arguments = Bundle().apply {
                            list[position].userId?.let { putInt("member_id", it) }
                            pantry_id?.let { putInt("pantry_id", it) }
                            Log.d("retrofit", list[position].userId.toString())
                            Log.d("retrofit", pantry_id.toString())
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
                    setRcvList(it.data)


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

    private fun setRcvList(it : ResponseShareMemberDto){
        val isOwner = it.isUserOwner
        if (isOwner != null) {
            checkOwnerAndSetVisibilty(isOwner)
            val adapter = ShareMemberAdapter(requireContext(), isOwner)

            binding.rcvHomeShareCodeItemMemberList.adapter = adapter
            val pantryList = it.list
            adapter.submitList(pantryList)
            it.list?.let { list -> clickItem(adapter, list) }
        }

    }

    private fun observeSearch() {
        viewModelShareMemberSearch.shareMemberSearch.observe(this) {
            when (it) {
                is UiState.Success -> {
                    setRcvList(it.data)
                }

                else -> Unit
            }
        }
    }

    private fun stopSearch() {
        val pantryId = arguments?.getInt("pantry_id")
        binding.ivHomeShareCodeSearchClose.setOnClickListener {
            binding.etHomeShareCodeSearch.setText("")
            binding.ivHomeShareCodeSearchClose.visibility = View.GONE
            if (pantryId != null) {
                viewModelShareMemberList.getShareCodeMember(pantryId = pantryId)
            }
        }
    }

    private fun checkDateValidate() {
        val pantryId = arguments?.getInt("pantry_id")
        binding.etHomeShareCodeSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.ivHomeShareCodeSearchClose.visibility = View.VISIBLE
            }

            override fun afterTextChanged(text: Editable?) {
                if (pantryId != null) {
                    if (text.toString().isEmpty()) {
                        viewModelShareMemberList.getShareCodeMember(pantryId)
                    } else {
                        if (text.toString().contains("\n")) {
                            val searchKeyWord = binding.etHomeShareCodeSearch.text.toString().trim()
                            viewModelShareMemberSearch.getShareMemberSearch(
                                pantryId, searchKeyWord
                            )
                            binding.etHomeShareCodeSearch.setText(searchKeyWord)
                        }
                    }
                }
            }
        }
        )
    }
    companion object {
        const val POP_UP_MEMEBER_DELETE = "share_invitation_to_delete_member"
    }
}