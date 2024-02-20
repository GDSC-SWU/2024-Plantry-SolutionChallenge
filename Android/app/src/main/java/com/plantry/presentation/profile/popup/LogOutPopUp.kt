package com.plantry.presentation.profile.popup

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.viewModels
import com.plantry.R
import com.plantry.coreui.base.BindingDialogFragment
import com.plantry.coreui.view.UiState
import com.plantry.databinding.PopupProfileLogoutBinding
import com.plantry.presentation.auth.viewmodel.LogoutViewModel
import com.plantry.presentation.auth.ui.SignInActivity

class LogOutPopUp  : BindingDialogFragment<PopupProfileLogoutBinding>(R.layout.popup_profile_logout) {

    private val viewModelMissionList by viewModels<LogoutViewModel>()

    override fun initView() {
        clickCancleButton()
        clickLogOutButton()
        observe()

    }

    private fun clickLogOutButton(){
        binding.tvProfileLogoutPopupDelete.setOnClickListener {
            viewModelMissionList.deleteGoogleLogout()
        }
    }

    private fun clickCancleButton(){
        binding.tvProfileLogoutPopupCancle.setOnClickListener {
            dismiss()
        }
    }

    private fun observe(){
        viewModelMissionList.logout.observe(this) {
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
            startActivity(this)
        }
    }
}