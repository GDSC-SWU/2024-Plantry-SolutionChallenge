package com.plantry.presentation.share.adapter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.plantry.presentation.share.ui.FragmentShareInvitation
import com.plantry.presentation.share.ui.FragmentShareRegister

private const val NUM_TABS = 2

class ShareCodeViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, val pantryId: Int?) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        val invitationFragment = FragmentShareInvitation()
        invitationFragment.arguments = Bundle().apply {
            if (pantryId != null) {
                putInt("pantry_id", pantryId)
                Log.d("Aaa", pantryId.toString())
            }
        }
        when (position) {
            0 -> {
                return invitationFragment
            }
            1 -> {
                val registrationFragment = FragmentShareRegister()
                registrationFragment.arguments = Bundle().apply {
                    if (pantryId != null) {
                        putInt("pantry_id", pantryId)
                        Log.d("Aaa", pantryId.toString())
                    }
                }
                return registrationFragment
            }
        }
        return invitationFragment
    }
}