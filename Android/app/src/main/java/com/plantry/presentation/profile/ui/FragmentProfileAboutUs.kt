package com.plantry.presentation.profile.ui

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.navigation.fragment.findNavController
import com.plantry.R
import com.plantry.coreui.base.BindingFragment
import com.plantry.databinding.FragmentProfileAboutUsBinding

class FragmentProfileAboutUs :
    BindingFragment<FragmentProfileAboutUsBinding>(R.layout.fragment_profile_about_us) {
    override var bnvVisibility = View.GONE

    override fun initView() {
        val recipientEmail = "gdscswu.plantry.0@gmail.com"

        binding.tvProfileAboutUsConfirm.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
            }
            startActivity(emailIntent)
        }
        clickBackStack()
    }


    private fun clickBackStack(){
        binding.ivProfileAboutUsToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}