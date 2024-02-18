package com.plantry.presentation.home.popup

import android.util.Log
import androidx.fragment.app.viewModels
import com.plantry.R
import com.plantry.coreui.base.BindingDialogFragment
import com.plantry.coreui.view.UiState
import com.plantry.databinding.PopupHomeDeleteBinding
import com.plantry.presentation.home.viewmodel.pantry.PantryDeleteViewModel
import com.plantry.presentation.home.viewmodel.pantry.PantryListViewModel

class HomeDeletePopUp :
    BindingDialogFragment<PopupHomeDeleteBinding>(R.layout.popup_home_delete) {
    private val viewModel_delete by viewModels<PantryDeleteViewModel>()
    private val viewModel_list by viewModels<PantryListViewModel>({ requireParentFragment() })

    override fun initView() {
        clickCancleButton()
        clickDeleteButton()
        observe()
    }

    private fun clickDeleteButton(){
        binding.tvHomeDeletePopupDelete.setOnClickListener {
            val pantry_id = arguments?.getInt("pantry_id")

            if (pantry_id != null) {
                Log.d("aaa", pantry_id.toString())
                viewModel_delete.deletePantry(pantry_id)
            }
        }
    }
    private fun clickCancleButton(){
        binding.tvHomeDeletePopupCancle.setOnClickListener {
            dialog?.dismiss()
        }
    }


    private fun observe() {
        viewModel_delete.pantryDelete.observe(this) {
            when (it) {
                is UiState.Success -> {
                    dialog?.dismiss()
                    viewModel_list.getPantryList()
                    Log.d("Aaa13", "deleted")
                }

                else -> Unit
            }
        }
    }
}