package com.plantry.presentation.home.popup

import com.plantry.R
import com.plantry.coreui.base.BindingDialogFragment
import com.plantry.databinding.PopupHomeDeleteBinding

class HomeDeletePopUp :
    BindingDialogFragment<PopupHomeDeleteBinding>(R.layout.popup_home_delete) {
    override fun initView() {
        clickCancleButton()
    }

    private fun clickCancleButton(){
        binding.tvHomeDeletePopupCancle.setOnClickListener {
            dialog?.dismiss()
        }
    }
}