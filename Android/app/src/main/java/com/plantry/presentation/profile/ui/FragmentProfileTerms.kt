package com.plantry.presentation.profile.ui

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.plantry.R
import com.plantry.coreui.base.BindingFragment
import com.plantry.coreui.view.UiState
import com.plantry.databinding.FragmentProfileTermsBinding
import com.plantry.presentation.profile.viewmodel.ProfileTermViewModel
import com.plantry.presentation.profile.viewmodel.ProfileTermViewModel.Companion.ALL

class FragmentProfileTerms :
    BindingFragment<FragmentProfileTermsBinding>(R.layout.fragment_profile_terms){
    override var bnvVisibility = View.GONE
    private val viewModelTerm by viewModels<ProfileTermViewModel>()
    override fun initView() {
        clickBackStack()
        viewModelTerm.getTermProfile(ALL)
        observe()
    }

    private fun clickBackStack(){
        binding.ivProfileTermsToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun observe(){
        viewModelTerm.termItem.observe(this) {
            when (it) {
                is UiState.Success -> {
                    binding.tvProfileTermsTitleUse.setText(it.data.result?.get(0)?.title)
                    binding.tvProfileTermsContentUse.setText(it.data.result?.get(0)?.content)
                    binding.tvProfileTermsTitlePrivacy.setText(it.data.result?.get(1)?.title)
                    binding.tvProfileTermsContentPrivacy.setText(it.data.result?.get(1)?.content)
                    Log.d("aaa", it.data.toString())
                }

                else -> Unit
            }
        }
    }
}