package com.plantry.presentation.addfood.popup.addfood

import com.plantry.R
import com.plantry.coreui.base.BindingDialogFragment
import com.plantry.databinding.PopupAddFoodDateBinding

class AddFoodDatePopUp :
    BindingDialogFragment<PopupAddFoodDateBinding>(R.layout.popup_add_food_date) {

    override fun initView() {
        clickCancleButton()
    }

    private fun clickCancleButton(){
        binding.tvAddFoodPopupConfirm.setOnClickListener {
            dialog?.dismiss()
        }
    }
}