package com.plantry.presentation.home.bottomsheet

import android.os.Bundle
import androidx.navigation.fragment.findNavController
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
            val pantry_id = arguments?.getInt("pantry_id")
            pantryDeletePopUp.arguments = Bundle().apply {
                if (pantry_id != null) {
                    putInt("pantry_id", pantry_id)
                }
            }
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
            val pantryId = arguments?.getInt("pantry_id")
            val pantryTitle = arguments?.getString("pantry_title")
            val pantryColor = arguments?.getString("pantry_color")
            pantryPlusPopUp.arguments = Bundle().apply {
                if (pantryId != null) {
                    putInt("pantry_id", pantryId)
                    putString("pantry_title", pantryTitle)
                    putString("pantry_color", pantryColor)
                }
            }
            pantryPlusPopUp.show(parentFragmentManager, POP_UP)
        }
    }

    companion object {
        const val POP_UP = "home_delete_pop_up"
    }

}