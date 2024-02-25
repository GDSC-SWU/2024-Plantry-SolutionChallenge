package com.plantry.presentation.share.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.plantry.R
import com.plantry.coreui.base.BindingFragment
import com.plantry.coreui.view.UiState
import com.plantry.databinding.FragmentShareCodeBinding
import com.plantry.presentation.share.adapter.ShareCodeViewPagerAdapter
import com.plantry.presentation.share.viewmodel.ShareMemberViewModel

class FragmentShareCode :
    BindingFragment<FragmentShareCodeBinding>(R.layout.fragment_share_code) {

    override var bnvVisibility = View.GONE
    private val viewModelShareMemberList by viewModels<ShareMemberViewModel>({ requireParentFragment() })

    override fun initView() {
        getShareMemberList()
        clickBackStack()
        observeMemberList()
    }

    private fun getShareMemberList(){
        val pantryId = arguments?.getInt("pantry_id")
        pantryId?.let {
            viewModelShareMemberList.getShareCodeMember(it)
        }
    }

    private fun clickBackStack() {
        binding.ivHomeShareCodeBack.setOnClickListener {
            val pantryName = arguments?.getString("pantry_name")
            val pantryId = arguments?.getInt("pantry_id")

            val arguments = Bundle().apply {
                if (pantryName != null && pantryId != null) {
                    putString("pantry_name", pantryName)
                    putInt("pantry_id", pantryId)
                }
            }
            findNavController().navigate(R.id.action_share_code_to_home_pantry, arguments)
        }
    }

    private fun setViewpagerTabTitle(isOwner : Boolean, memberCount : Int) {
        val pantryId = arguments?.getInt("pantry_id")

        binding.vpShareCodeContent.adapter = ShareCodeViewPagerAdapter(parentFragmentManager, lifecycle, pantryId)
        TabLayoutMediator(binding.tlHomeShareCodeTab, binding.vpShareCodeContent) { tab, position ->
            when (position) {
                0 -> {
                    if(isOwner){
                        tab.text = resources.getString(R.string.home_share_code_invitation)
                    }else{
                        tab.text = "Member (${memberCount})"
                    }
                }

                1 -> {
                    tab.text = resources.getString(R.string.home_share_code_registration)
                }
            }
        }.attach()
    }

    private fun observeMemberList(){
        viewModelShareMemberList.shareMember.observe(this) {
            when (it) {
                is UiState.Success -> {
                    if(it.data.isUserOwner != null &&  it.data.list != null){
                        setViewpagerTabTitle(it.data.isUserOwner, it.data.list.size)
                    }
                }

                else -> Unit
            }
        }
    }
}