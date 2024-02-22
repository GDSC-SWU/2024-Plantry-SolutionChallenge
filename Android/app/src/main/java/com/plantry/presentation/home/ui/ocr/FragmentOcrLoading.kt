package com.plantry.presentation.home.ui.ocr

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.plantry.R
import com.plantry.coreui.base.BindingFragment
import com.plantry.coreui.fragment.toast
import com.plantry.coreui.view.UiState
import com.plantry.databinding.FragmentOcrLoadingBinding
import com.plantry.presentation.addfood.popup.ocr.MultiAddFoodPopUp
import com.plantry.presentation.addfood.viewmodel.ocr.OcrSubmitViewModel

class FragmentOcrLoading :
    BindingFragment<FragmentOcrLoadingBinding>(R.layout.fragment_ocr_loading) {

    override var bnvVisibility: Int = View.GONE
    private val viewModelOcrSubmit by viewModels<OcrSubmitViewModel>({ requireActivity() })

    override fun initView() {
        observeOcr()
    }

    fun observeOcr() {
        viewModelOcrSubmit.ocrResult.observe(this) {
            when (it) {
                is UiState.Success -> {
                    findNavController().popBackStack()
                    if (it.data.data?.size != 0 && it.data.data != null) {
                        val addFoodPopup = MultiAddFoodPopUp()
                        addFoodPopup.setStyle(
                            BottomSheetDialogFragment.STYLE_NO_TITLE,
                            R.style.Theme_Plantry_AlertDialog
                        )
                        addFoodPopup.arguments = Bundle().apply {
                            putInt("itemCount", it.data.data.size)
                        }
                        addFoodPopup.show(parentFragmentManager, "")
                    } else {
                        toast("There are no recognized products.")
                    }
                }

                else -> Unit
            }
        }
    }
}