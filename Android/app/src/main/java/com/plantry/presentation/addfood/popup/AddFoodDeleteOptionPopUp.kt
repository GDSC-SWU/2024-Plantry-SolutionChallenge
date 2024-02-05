package com.plantry.presentation.addfood.popup

import com.plantry.R
import com.plantry.coreui.base.BindingDialogFragment
import com.plantry.databinding.PopupAddFoodBinding
import com.plantry.databinding.PopupAddFoodDeleteOptionBinding

class AddFoodDeleteOptionPopUp :
    BindingDialogFragment<PopupAddFoodDeleteOptionBinding>(R.layout.popup_add_food_delete_option) {

    override fun initView() {
        clickCancleButton()
    }

    private fun clickCancleButton() {

        dialog?.dismiss()
    }
}
