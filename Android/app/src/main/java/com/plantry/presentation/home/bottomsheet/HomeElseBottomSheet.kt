package com.plantry.presentation.home.bottomsheet

import com.plantry.R
import com.plantry.coreui.base.BindingBottomSheetFragment
import com.plantry.databinding.BottomsheetHomeElseBinding
import com.plantry.presentation.home.popup.HomeDeletePopUp

class HomeElseBottomSheet :
    BindingBottomSheetFragment<BottomsheetHomeElseBinding>(R.layout.bottomsheet_home_else) {
    override fun initView() {
        clickDelete()
    }

    private fun clickDelete() {
        binding.tvHomeBottomSheetDelete.setOnClickListener {
            val pantryDeletePopUp = HomeDeletePopUp()
            pantryDeletePopUp.setStyle(STYLE_NO_TITLE, R.style.Theme_Plantry_AlertDialog)
            pantryDeletePopUp.show(parentFragmentManager, POP_UP)
        }
    }

    private fun clickEdit() {
        binding.tvHomeBottomSheetEdit.setOnClickListener {
            // 페이지 이동 및 아이디 넘기는 로직 구현 예정
        }
    }

    companion object {
        const val POP_UP = "home_delete_pop_up"
    }

}