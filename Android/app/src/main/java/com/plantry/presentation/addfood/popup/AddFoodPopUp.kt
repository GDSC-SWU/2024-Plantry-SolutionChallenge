package com.plantry.presentation.addfood.popup

import com.plantry.R
import com.plantry.coreui.base.BindingDialogFragment
import com.plantry.databinding.PopupAddFoodBinding

class AddFoodPopUp :
    BindingDialogFragment<PopupAddFoodBinding>(R.layout.popup_add_food) {

    override fun initView() {
        clickCancleButton()
    }

    private fun clickCancleButton(){
        binding.tvAddFoodPopupCancle.setOnClickListener {
            dialog?.dismiss()
        }
    }
}
