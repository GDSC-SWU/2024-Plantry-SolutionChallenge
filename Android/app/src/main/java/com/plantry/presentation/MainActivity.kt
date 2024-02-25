package com.plantry.presentation

import android.content.Context
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.plantry.R
import com.plantry.coreui.base.BindingActivity
import com.plantry.coreui.context.toast
import com.plantry.coreui.view.UiState
import com.plantry.databinding.ActivityMainBinding
import com.plantry.presentation.addfood.ui.FragmentAddFood
import com.plantry.presentation.auth.ui.SignInActivity
import com.plantry.presentation.auth.ui.SignInActivity.Companion.ALARM_PERMITTED_USED_REQUESTED
import com.plantry.presentation.auth.ui.SignInActivity.Companion.ALARM_PERMITTED_USED
import com.plantry.presentation.profile.viewmodel.ProfileSetPermittedViewModel

class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModelSetPermitted by viewModels<ProfileSetPermittedViewModel>()

    override fun initView() {
        setNavigation()
        setDarkMode()
        checkAlarmState()
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

    private fun checkAlarmState(){
        val prefs = getSharedPreferences(ALARM_PERMITTED_USED, Context.MODE_PRIVATE)
        val alarmPermissionRequested = prefs.getBoolean(ALARM_PERMITTED_USED_REQUESTED, false)

        val prefsAlarm = getSharedPreferences(SignInActivity.ALARM_STATE, Context.MODE_PRIVATE)
        val alarmStateRequested = prefsAlarm.getBoolean(SignInActivity.ALARM_STATE_REQUESTED, true)

        if(alarmPermissionRequested){
            if(!alarmStateRequested){
                viewModelSetPermitted.patchProfilePermittedSet()
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