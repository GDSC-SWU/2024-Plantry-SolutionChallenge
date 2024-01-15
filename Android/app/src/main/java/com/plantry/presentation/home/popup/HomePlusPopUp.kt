package com.plantry.presentation.home.popup

import com.plantry.R
import com.plantry.coreui.base.BindingDialogFragment
import com.plantry.databinding.PopupHomePlusBinding

class HomePlusPopUp :
    BindingDialogFragment<PopupHomePlusBinding>(R.layout.popup_home_plus) {
    override fun initView() {
        clickCancleButton()
    }

    private fun clickCancleButton(){
        binding.tvHomePlusPopupCancle.setOnClickListener {
            dialog?.dismiss()
        }
    }

}