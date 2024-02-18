package com.plantry.presentation.home.popup

import android.util.Log
import androidx.fragment.app.viewModels
import com.plantry.R
import com.plantry.coreui.base.BindingDialogFragment
import com.plantry.coreui.view.UiState
import com.plantry.data.dto.request.pantry.RequestPantryAddDto
import com.plantry.databinding.PopupHomePlusBinding
import com.plantry.presentation.home.viewmodel.pantry.PantryAddViewModel
import com.plantry.presentation.home.viewmodel.pantry.PantryEditViewModel
import com.plantry.presentation.home.viewmodel.pantry.PantryListViewModel

class HomePlusPopUp :
    BindingDialogFragment<PopupHomePlusBinding>(R.layout.popup_home_plus) {
    private val viewModel_add by viewModels<PantryAddViewModel>()
    private val viewModel_edit by viewModels<PantryEditViewModel>()
    private val viewModel_list by viewModels<PantryListViewModel>({ requireParentFragment() })
    var checkedColor = "FFA5A0"

    override fun initView() {
        val pantry_id = arguments?.getInt("pantry_id")
        Log.d("ddd", pantry_id.toString())
        if (pantry_id != null) { // edit 으로 들어온 경우
            setEditTitle()
            setEditColor()
            clickCancleButton()
            getClickedColor()
            clickConfirmButton()
            observe_edit()

        } else { // plus 로 들어온 경우
            clickCancleButton()
            getClickedColor()
            clickConfirmButton()
            observe_add()
        }
    }

    private fun setEditTitle() {
        val pantryTitle = arguments?.getString("pantry_title")
        binding.etHomePlusPopupPantryName.setText(pantryTitle)
    }

    private fun setEditColor() {
        Log.d("ddd", arguments?.getString("pantry_color").toString())
        when (arguments?.getString("pantry_color")) {
            "FFA5A0" -> {
                checkedColor =  "FFA5A0"
                binding.rbHomePlusPopupColorRed.isChecked = true
            }

            "FFCE8A" -> {
                checkedColor =  "FFCE8A"
                binding.rbHomePlusPopupColorOrange.isChecked = true
            }

            "FFE88A" -> {
                checkedColor =  "FFE88A"
                binding.rbHomePlusPopupColorYellow.isChecked = true
            }

            "A2E5B3" -> {
                checkedColor =  "A2E5B3"
                binding.rbHomePlusPopupColorGreen.isChecked = true
            }

            "8AC2FF" -> {
                checkedColor =  "8AC2FF"
                binding.rbHomePlusPopupColorBlue.isChecked = true
            }

            "DAAFF0" -> {
                checkedColor =  "DAAFF0"
                binding.rbHomePlusPopupColorPurple.isChecked = true
            }
        }
    }

    private fun clickConfirmButton() {
        binding.tvHomePlusPopupConfirm.setOnClickListener {
            val title = binding.etHomePlusPopupPantryName.text.toString()
            val requestAddPantry = RequestPantryAddDto(title, checkedColor)

            Log.d("ddd", checkedColor)
            val pantry_id = arguments?.getInt("pantry_id")
            if (pantry_id != null) { // edit 으로 들어온 경우
                viewModel_edit.patchEditPantry(pantry_id, requestAddPantry)
            } else {
                viewModel_add.postAddPantry(requestAddPantry)
            }
        }
    }


    private fun getClickedColor() {
        binding.rgHomePlusPopupColorSelect.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_home_plus_popup_color_red -> {
                    checkedColor = "FFA5A0"
                }

                R.id.rb_home_plus_popup_color_orange -> {
                    checkedColor = "FFCE8A"
                }

                R.id.rb_home_plus_popup_color_yellow -> {
                    checkedColor = "FFE88A"
                }

                R.id.rb_home_plus_popup_color_green -> {
                    checkedColor = "A2E5B3"
                }

                R.id.rb_home_plus_popup_color_blue -> {
                    checkedColor = "8AC2FF"
                }

                R.id.rb_home_plus_popup_color_purple -> {
                    checkedColor = "DAAFF0"
                }
            }
        }
    }

    private fun clickCancleButton() {
        binding.tvHomePlusPopupCancle.setOnClickListener {
            dialog?.dismiss()
        }
    }


    private fun observe_add() {
        viewModel_add.pantryItem.observe(this) {
            when (it) {
                is UiState.Success -> {
                    dialog?.dismiss()
                    viewModel_list.getPantryList()
                    Log.d("Aaa13", it.data.toString())
                }

                else -> Unit
            }

        }
    }

    private fun observe_edit() {
        viewModel_edit.pantryEdit.observe(this) {
            when (it) {
                is UiState.Success -> {
                    dialog?.dismiss()
                    viewModel_list.getPantryList()
                    Log.d("Aaa13", it.data.toString())
                }

                else -> Unit
            }

        }
    }
}