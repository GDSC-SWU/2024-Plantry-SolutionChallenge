package com.plantry.presentation.profile.popup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import com.plantry.R
import com.plantry.coreui.base.BindingDialogFragment
import com.plantry.coreui.view.UiState
import com.plantry.databinding.PopupProfileDeleteBinding
import com.plantry.presentation.auth.ui.SignInActivity
import com.plantry.presentation.auth.viewmodel.SignoutViewModel

class DeleteIdPopUp :
    BindingDialogFragment<PopupProfileDeleteBinding>(R.layout.popup_profile_delete) {
    private val viewModelDeleteId by viewModels<SignoutViewModel>()

    override fun initView() {
        clickCanle()
        clickDelete()
        observe()
    }

    private fun clickCanle() {
        binding.tvProfileDeletePopupCancle.setOnClickListener {
            dismiss()
        }
    }

    private fun clickDelete() {
        binding.tvProfileDeletePopupDelete.setOnClickListener {
            val deleteOption = arguments?.getInt("deleteOption") ?: 1
            viewModelDeleteId.deleteGoogleSignout(deleteOption)
        }
    }


    private fun observe(){
        viewModelDeleteId.signout.observe(this) {
            when (it) {
                is UiState.Success -> {
                    navigateTo<SignInActivity>()
                }

                else -> Unit
            }
        }
    }

    private inline fun <reified T : Activity> navigateTo() {
        Intent(requireActivity(), T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra("sign_state", SIGNOUT)
            startActivity(this, )
        }
    }

    companion object{
        const val SIGNOUT = 2
    }
}