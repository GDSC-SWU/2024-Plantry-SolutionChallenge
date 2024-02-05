package com.plantry.presentation.home.bottomsheet

import com.plantry.R
import com.plantry.coreui.base.BindingBottomSheetFragment
import com.plantry.databinding.BottomsheetHomeElseBinding
import com.plantry.presentation.home.popup.HomeDeletePopUp
import com.plantry.presentation.home.popup.HomePlusPopUp

class HomeElseBottomSheet :
    BindingBottomSheetFragment<BottomsheetHomeElseBinding>(R.layout.bottomsheet_home_else) {
    override fun initView() {
        clickDelete()
        clickEdit()
    }

    private fun clickDelete() {
        binding.tvHomeBottomSheetDelete.setOnClickListener {
            val pantryDeletePopUp = HomeDeletePopUp()
            pantryDeletePopUp.setStyle(
                STYLE_NO_TITLE,
                R.style.Theme_Plantry_AlertDialog
            )
            pantryDeletePopUp.show(parentFragmentManager, POP_UP)
        }
    }

    private fun clickEdit() {
        binding.tvHomeBottomSheetEdit.setOnClickListener {
            val pantryPlusPopUp = HomePlusPopUp()
            pantryPlusPopUp.setStyle(
                STYLE_NO_TITLE,
                R.style.Theme_Plantry_AlertDialog
            )
            pantryPlusPopUp.show(parentFragmentManager, POP_UP)
        }
    }

    companion object {
        const val POP_UP = "home_delete_pop_up"
    }

}