package com.plantry.presentation.share.popup

import android.os.Handler
import android.view.View
import androidx.fragment.app.viewModels
import com.plantry.R
import com.plantry.coreui.base.BindingDialogFragment
import com.plantry.coreui.view.UiState
import com.plantry.databinding.PopupShareJoinBinding
import com.plantry.presentation.share.viewmodel.ShareCodeSubmitViewModel

class ShareJoinPopUp :
    BindingDialogFragment<PopupShareJoinBinding>(R.layout.popup_share_join) {
    private val viewModelSubmit by viewModels<ShareCodeSubmitViewModel>({ requireParentFragment() })

    override fun initView() {
        clickClick()
        clickConfrim()
        observeSubmitResult()
    }

    private fun clickClick() {
        binding.tvHomeShareJoinPopupCancle.setOnClickListener {
            dismiss()
        }
    }

    private fun clickConfrim() {
        binding.tvHomeShareJoinPopupConfrim.setOnClickListener {
            val submitCode = binding.etHomeShareJoinPopupInvitationCode.text.toString()
            viewModelSubmit.postShareCodeSubmit(submitCode)
        }
    }

    override fun dismiss() {
        super.dismiss()
        viewModelSubmit.setShareCodeFaliure()
    }

    private fun observeSubmitResult() {
        viewModelSubmit.shareCodeSubmit.observe(this) {
            when (it) {
                is UiState.Success -> {
                    binding.tvShareJoinErrorMessage.visibility = View.GONE
                    dismiss()
                }
                is UiState.Failure -> {
                    if(it.msg.equals("HTTP 404 ")) {
                        binding.tvShareJoinErrorMessage.visibility = View.VISIBLE
                        binding.etHomeShareJoinPopupInvitationCode.text = null
                    }
                }

                else -> Unit
            }
        }
    }
}