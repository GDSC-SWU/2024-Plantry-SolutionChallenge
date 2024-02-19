package com.plantry.presentation.profile.popup

import com.plantry.R
import com.plantry.coreui.base.BindingDialogFragment
import com.plantry.databinding.PopupProfileDeleteFoodBinding

class DeleteFoodPopUp :
    BindingDialogFragment<PopupProfileDeleteFoodBinding>(R.layout.popup_profile_delete_food) {
    override fun initView() {
        clickConfirm()
    }

    private fun clickConfirm() {
        binding.tvProfileDeleteFoodConfirm.setOnClickListener {
            dismiss()
        }
    }

}