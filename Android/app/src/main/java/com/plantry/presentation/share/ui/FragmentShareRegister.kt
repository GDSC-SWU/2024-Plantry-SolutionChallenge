package com.plantry.presentation.share.ui

import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.plantry.R
import com.plantry.coreui.base.BindingFragment
import com.plantry.coreui.view.UiState
import com.plantry.databinding.FragmentProfileTermsBinding
import com.plantry.databinding.FragmentShareCodeBinding
import com.plantry.databinding.FragmentShareCodeRegistrationBinding
import com.plantry.presentation.home.adapter.pantry.PantryDayAdapter
import com.plantry.presentation.profile.viewmodel.ProfileTermViewModel
import com.plantry.presentation.profile.viewmodel.ProfileTermViewModel.Companion.ALL
import com.plantry.presentation.share.viewmodel.ShareCodeSubmitViewModel
import com.plantry.presentation.share.viewmodel.ShareMemberViewModel

class FragmentShareRegister :
    BindingFragment<FragmentShareCodeRegistrationBinding>(R.layout.fragment_share_code_registration){
    override var bnvVisibility = View.GONE
    private val viewModelSubmit by viewModels<ShareCodeSubmitViewModel>()


    override fun initView() {
        checkTextIsExist()
        observeSubmitResult()
    }

    private fun checkTextIsExist() {
        binding.etHomeShareCodeRegisterSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                setConfirmTextColor(editable.toString().isNotEmpty())
            }
        })
    }


    private fun setConfirmTextColor(isTextExist : Boolean){
        if(isTextExist){
            binding.tvShareCodeRegisterConfirm.setTextColor(resources.getColor(R.color.white1))
            clickConfirm()
        }
        else{
            binding.tvShareCodeRegisterConfirm.setTextColor(resources.getColor(R.color.gray1))
        }
    }

    private fun clickConfirm(){
        binding.tvShareCodeRegisterConfirm.setOnClickListener {
            viewModelSubmit.postShareCodeSubmit(binding.etHomeShareCodeRegisterSearch.text.toString())
        }
    }

    private fun observeSubmitResult() {
        viewModelSubmit.shareCodeSubmit.observe(this) {
            when (it) {
                is UiState.Success -> {
                    binding.tvShareCodeSuccess.visibility = View.VISIBLE
                    Handler().postDelayed({
                        binding.tvShareCodeSuccess.visibility = View.GONE
                    }, 1000)
                }

                is UiState.Failure -> {
                   if(it.msg.equals("HTTP 404 ")) {
                       binding.tvShareCodeFailure.visibility = View.VISIBLE
                       Handler().postDelayed({
                           binding.tvShareCodeFailure.visibility = View.GONE
                       }, 1000)
                   }
                }

                else -> Unit
            }
        }
    }
}