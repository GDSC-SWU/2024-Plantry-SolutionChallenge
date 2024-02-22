package com.plantry.presentation.share.popup

import androidx.fragment.app.viewModels
import com.plantry.R
import com.plantry.coreui.base.BindingDialogFragment
import com.plantry.databinding.PopupProfileDeleteFoodBinding
import com.plantry.databinding.PopupShareMemberDeleteBinding
import com.plantry.presentation.share.viewmodel.ShareMemberViewModel

class DeleteMemberPopUp :
    BindingDialogFragment<PopupShareMemberDeleteBinding>(R.layout.popup_share_member_delete) {

    private val viewModelShareMemberList by viewModels<ShareMemberViewModel>({ requireParentFragment() })


    override fun initView() {
        clickClick()
    }

    private fun clickClick() {
        binding.tvHomeShareMemberDeletePopupCancle.setOnClickListener {
            dismiss()
        }
    }

    private fun clickDelete(){
        // 확인 누르면 받은 memberId 전달해서 member 삭제하기
        binding.tvHomeShareMemberDeletePopupDelete.setOnClickListener {
            dismiss()
        }
    }

    // observe 성공하면 list 다시 부르고 dismiss() 하기!!

}