package com.plantry.presentation.home.ui

import android.view.View
import androidx.navigation.fragment.findNavController
import com.plantry.R
import com.plantry.coreui.base.BindingFragment
import com.plantry.databinding.FragmentHomePantryBinding


class FragmentHomePantry :
    BindingFragment<FragmentHomePantryBinding>(R.layout.fragment_home_pantry) {

    override var bnvVisibility = View.GONE

    override fun initView() {
        setPantryName()
        backToHome()
    }

    private fun setPantryName() {
        val pantry_name = arguments?.getString("pantry_name")
        binding.tvHomePantryName.setText(pantry_name)
    }

    private fun backToHome(){
        binding.ivHomePantryBack.setOnClickListener {
            findNavController().navigate(R.id.action_home_pantry_to_home)
        }
    }


}