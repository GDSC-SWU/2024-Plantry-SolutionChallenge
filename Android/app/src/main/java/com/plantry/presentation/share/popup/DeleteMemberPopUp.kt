package com.plantry.presentation.share.popup

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.plantry.R
import com.plantry.coreui.base.BindingDialogFragment
import com.plantry.coreui.fragment.toast
import com.plantry.coreui.view.UiState
import com.plantry.databinding.PopupProfileDeleteFoodBinding
import com.plantry.databinding.PopupShareMemberDeleteBinding
import com.plantry.presentation.share.viewmodel.ShareMemberDeleteViewModel
import com.plantry.presentation.share.viewmodel.ShareMemberViewModel

class DeleteMemberPopUp :
    BindingDialogFragment<PopupShareMemberDeleteBinding>(R.layout.popup_share_member_delete) {

    private val viewModelShareMemberList by viewModels<ShareMemberViewModel>({ requireParentFragment() })
    private val viewModelMemberDelete by viewModels<ShareMemberDeleteViewModel>()


    override fun initView() {
        clickClick()
        clickDelete()
        observeDelete()
    }

    private fun clickClick() {
        binding.tvHomeShareMemberDeletePopupCancle.setOnClickListener {
            dismiss()
        }
    }

    private fun clickDelete(){
        binding.tvHomeShareMemberDeletePopupDelete.setOnClickListener {
            val pantry_id = arguments?.getInt("pantry_id")
            val member_id = arguments?.getInt("member_id")
            Log.d("retrofit", member_id.toString())
            Log.d("retrofit", pantry_id.toString())

            if (pantry_id != null && member_id != null) {
                viewModelMemberDelete.deleteShareMember(pantryId = pantry_id, userId = member_id)
            }
        }
    }

    private fun observeDelete(){
        viewModelMemberDelete.shareMemberDelete.observe(this) {
            when (it) {
                is UiState.Success -> {
                    val pantry_id = arguments?.getInt("pantry_id")
                    if (pantry_id != null) {
                        viewModelShareMemberList.getShareCodeMember(pantryId = pantry_id)
                        dismiss()
                    }
                }
                is UiState.Failure -> {
                    if(it.msg.equals("HTTP 403 ")) {
                        toast("Sorry, You are not the owner of the refrigerator, so you cannot delete it.")
                        dismiss()
                    }
                }

                else -> Unit
            }
        }

    }

}