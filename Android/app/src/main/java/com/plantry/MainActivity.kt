package com.plantry

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.plantry.coreui.base.BindingActivity
import com.plantry.databinding.ActivityMainBinding

class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDarkMode()
        initView()
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
    }

    // 라이트 모드로만 제공하기 위한 함수
    private fun setDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}