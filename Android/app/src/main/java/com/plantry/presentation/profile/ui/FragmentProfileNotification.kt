package com.plantry.presentation.profile.ui

import android.view.View
import androidx.navigation.fragment.findNavController
import com.plantry.R
import com.plantry.coreui.base.BindingFragment
import com.plantry.databinding.FragmentProfileNotificationBinding

class FragmentProfileNotification :
    BindingFragment<FragmentProfileNotificationBinding>(R.layout.fragment_profile_notification){

    override var bnvVisibility = View.GONE
    override fun initView() {
        clickBackStack()
    }


    private fun clickBackStack(){
        binding.ivProfileAlarmToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}