package com.plantry

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.plantry.coreui.base.BindingActivity
import com.plantry.databinding.ActivityMainBinding
import com.plantry.presentation.addfood.ui.FragmentAddFood

class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDarkMode()
        initView()
        bnvAddFoodClicked()
    }

    override fun initView() {
        val navController =
            supportFragmentManager.findFragmentById(R.id.fcv_main_container)?.findNavController()

        with(binding) {
            bnvMainNavigation.itemIconTintList = null
            navController?.let {
                bnvMainNavigation.setupWithNavController(it)
            }
        }

        if(!(binding.ivMainPlus.hasWindowFocus())){
            binding.bnvMainNavigation.selectedItemId = R.id.navigation_home

        }


        binding.ivMainPlus.setOnClickListener {
            binding.bnvMainNavigation.selectedItemId = R.id.navigation_add_food
        }
    }


    fun bnvAddFoodClicked() {
        binding.bnvMainNavigation.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.navigation_add_food) {
                val addFoodPopup = FragmentAddFood()
                addFoodPopup.setStyle(
                    BottomSheetDialogFragment.STYLE_NO_TITLE,
                    R.style.Theme_Plantry_AlertDialog
                )
                addFoodPopup.show(supportFragmentManager, "")
            }
            true
        }
    }

    // 바텀 네비게이션 가리기
    fun setBottomNavigationVisibility(visibility: Int) {
        binding.bnvMainNavigation.visibility = visibility
        binding.ivMainPlus.visibility = visibility
        binding.vBnvGradation.visibility=visibility
    }

    // 라이트 모드로만 제공하기 위한 함수
    private fun setDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}