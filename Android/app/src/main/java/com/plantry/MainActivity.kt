package com.plantry

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.plantry.coreui.base.BindingActivity
import com.plantry.databinding.ActivityMainBinding
import com.plantry.presentation.addfood.ui.FragmentAddFood

class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun initView() {
        setNavigation()
        setDarkMode()
    }


    private fun setNavigation() {
        val navController =
            supportFragmentManager.findFragmentById(R.id.fcv_main_container)?.findNavController()

        with(binding) {
            bnvMainNavigation.itemIconTintList = null
            navController?.let {
                bnvMainNavigation.setupWithNavController(it)
                ivMainPlus.setOnClickListener { _ ->
                    val addFoodPopup = FragmentAddFood()
                    addFoodPopup.setStyle(
                        BottomSheetDialogFragment.STYLE_NO_TITLE,
                        R.style.Theme_Plantry_AlertDialog
                    )
                    addFoodPopup.show(supportFragmentManager, "")
                }
            }
        }
    }


    // 바텀 네비게이션 가리기
    fun setBottomNavigationVisibility(visibility: Int) {
        binding.bnvMainNavigation.visibility = visibility
        binding.ivMainPlus.visibility = visibility
        binding.vBnvGradation.visibility = visibility
    }

    // 라이트 모드로만 제공하기 위한 함수
    private fun setDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}