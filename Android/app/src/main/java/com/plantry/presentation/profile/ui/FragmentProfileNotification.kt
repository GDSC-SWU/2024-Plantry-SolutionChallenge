package com.plantry.presentation.profile.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.plantry.R
import com.plantry.coreui.base.BindingFragment
import com.plantry.coreui.view.UiState
import com.plantry.databinding.FragmentProfileNotificationBinding
import com.plantry.presentation.profile.viewmodel.ProfileAlarmTimeViewModel
import com.plantry.presentation.profile.viewmodel.ProfileAlarmViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FragmentProfileNotification :
    BindingFragment<FragmentProfileNotificationBinding>(R.layout.fragment_profile_notification) {

    private val viewModelAlarmTime by viewModels<ProfileAlarmTimeViewModel>()
    private val viewModelAlarmTimeEdit by viewModels<ProfileAlarmViewModel>()

    override var bnvVisibility = View.GONE


    override fun initView() {
        setNotificationPermission()
        clickBackStack()

        observeAlarmTime()
        observeAlarmTimeEdit()

        setAllNotificationView()
        setMarketingNotificationView()
        setDeadLineNotificationView()
        clickTimeContent()

        setDeadLine()
    }

    private fun setNotificationPermission() {
        val initNotificationPermission = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (initNotificationPermission) {
            setAllNotificationVisibilty(View.VISIBLE)
            binding.tvProfileAlarmAllowNotificationToggle.isSelected = true

        } else {
            setAllNotificationVisibilty(View.GONE)
            binding.tvProfileAlarmAllowNotificationToggle.isSelected = false

        }
    }

    private fun observeAlarmTime() {
        viewModelAlarmTime.alarmTime.observe(this) {
            when (it) {
                is UiState.Success -> {
                    if (it.data != 0) {
                        binding.tvProfileAlarmProductDeadlineNotificationToggle.isSelected = true
                        binding.tvProfileAlarmTimeContent.setText(convertToHourFormat(it.data))
                        setRadioGroupCheckedItem(it.data)
                    } else {
                        binding.tvProfileAlarmProductDeadlineNotificationToggle.isSelected = false
                    }
                }

                else -> Unit
            }
        }
    }

    private fun observeAlarmTimeEdit() {
        viewModelAlarmTimeEdit.alarmItem.observe(this) {
            when (it) {
                is UiState.Success -> {
                    binding.tvProfileAlarmTimeContent.text = convertToHourFormat(it.data)
                    binding.layoutAlarmTimeList.clAlarmPopupPantryList.visibility = View.GONE
                    binding.tvProfileAlarmTimeContent.isSelected = false
                }

                else -> Unit
            }
        }
    }

    private fun clickBackStack() {
        binding.ivProfileAlarmToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun convertToHourFormat(time: Int): String {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, time)
        }

        val dateFormat = SimpleDateFormat("h a", Locale.getDefault())
        val formattedTime = dateFormat.format(calendar.time).toString()
        val dotFormattedTime = formattedTime.replace("AM", "A.M").replace("PM", "P.M")

        return dotFormattedTime
    }

    // 시간 설정 부분 클릭
    private fun clickTimeContent() {
        binding.tvProfileAlarmTimeContent.setOnClickListener {
            binding.tvProfileAlarmTimeContent.isSelected =
                !binding.tvProfileAlarmTimeContent.isSelected
            if (binding.tvProfileAlarmTimeContent.isSelected) {
                binding.layoutAlarmTimeList.clAlarmPopupPantryList.visibility = View.VISIBLE
            } else {
                binding.layoutAlarmTimeList.clAlarmPopupPantryList.visibility = View.GONE
            }
        }
    }

    // 전체 알림 설정
    private fun setAllNotificationView() {
        binding.tvProfileAlarmAllowNotificationToggle.setOnClickListener {
            val intent =
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + requireActivity().packageName))
            notificationSettingsLauncher.launch(intent)
        }
    }

    private val notificationSettingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // 권한 설정 후의 처리 코드
            if (result.resultCode == Activity.RESULT_OK) { // 허락
                binding.tvProfileAlarmAllowNotificationToggle.isSelected = true
                setAllNotificationVisibilty(View.VISIBLE)
                viewModelAlarmTimeEdit.patchAlarmProfile(9)
                checkNotificationPermission()
            } else { // 거부
                binding.tvProfileAlarmAllowNotificationToggle.isSelected = false
                setAllNotificationVisibilty(View.GONE)
                viewModelAlarmTimeEdit.patchAlarmProfile(0)
            }
            setNotificationPermission()
        }

    private fun checkNotificationPermission() {
        // 권한에 따라 toggle 설정
        binding.tvProfileAlarmAllowNotificationToggle.isSelected =
            ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
    }

    private fun setMarketingNotificationView() {
        binding.tvProfileAlarmAllowMarketingNotificationToggle.setOnClickListener {
            binding.tvProfileAlarmAllowMarketingNotificationToggle.isSelected =
                !binding.tvProfileAlarmAllowMarketingNotificationToggle.isSelected
        }
    }

    private fun setDeadLineNotificationView() {
        binding.tvProfileAlarmProductDeadlineNotificationToggle.setOnClickListener {
            binding.tvProfileAlarmProductDeadlineNotificationToggle.isSelected =
                !binding.tvProfileAlarmProductDeadlineNotificationToggle.isSelected
            if (binding.tvProfileAlarmProductDeadlineNotificationToggle.isSelected) {
                binding.tvProfileAlarmTimeContent.visibility = View.VISIBLE
                viewModelAlarmTimeEdit.patchAlarmProfile(9)
            } else {
                binding.tvProfileAlarmTimeContent.visibility = View.GONE
                viewModelAlarmTimeEdit.patchAlarmProfile(0)
            }
        }
    }


    private fun setAllNotificationVisibilty(visiblity: Int) {
        binding.tvProfileAlarmProductDeadlineNotification.visibility = visiblity
        binding.tvProfileAlarmProductDeadlineNotificationToggle.visibility = visiblity
        binding.tvProfileAlarmAllowMarketingNotification.visibility = visiblity
        binding.tvProfileAlarmAllowMarketingNotificationToggle.visibility = visiblity
        if (binding.tvProfileAlarmProductDeadlineNotificationToggle.isSelected) {
            binding.tvProfileAlarmTimeContent.visibility = View.VISIBLE
        } else {
            binding.tvProfileAlarmTimeContent.visibility = View.GONE
        }
    }

    private fun setDeadLine() { // 리팩토링 하기
        var deadline: Int = 0
        binding.layoutAlarmTimeList.rgAlarmOptionGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_profile_alarm_content1 -> {
                    deadline = 9
                }

                R.id.rb_profile_alarm_content2 -> {
                    deadline = 10
                }

                R.id.rb_profile_alarm_content3 -> {
                    deadline = 11
                }

                R.id.rb_profile_alarm_content4 -> {
                    deadline = 12
                }

                R.id.rb_profile_alarm_content5 -> {
                    deadline = 13
                }

                R.id.rb_profile_alarm_content6 -> {
                    deadline = 14
                }

                R.id.rb_profile_alarm_content7 -> {
                    deadline = 15
                }

                R.id.rb_profile_alarm_content8 -> {
                    deadline = 16
                }

                R.id.rb_profile_alarm_content9 -> {
                    deadline = 17
                }

                R.id.rb_profile_alarm_content10 -> {
                    deadline = 18
                }

                R.id.rb_profile_alarm_content11 -> {
                    deadline = 19
                }

                R.id.rb_profile_alarm_content12 -> {
                    deadline = 20
                }

                R.id.rb_profile_alarm_content13 -> {
                    deadline = 21
                }

                R.id.rb_profile_alarm_content14 -> {
                    deadline = 22
                }

                R.id.rb_profile_alarm_content15 -> {
                    deadline = 23
                }

                R.id.rb_profile_alarm_content16 -> {
                    deadline = 24
                }
            }
            viewModelAlarmTimeEdit.patchAlarmProfile(deadline)
        }
    }

    private fun setRadioGroupCheckedItem(time: Int) { // 리팩토링 하기
        when (time) {
            9 -> {
                binding.layoutAlarmTimeList.rbProfileAlarmContent1.isChecked = true
            }

            10 -> {
                binding.layoutAlarmTimeList.rbProfileAlarmContent2.isChecked = true
            }

            11 -> {
                binding.layoutAlarmTimeList.rbProfileAlarmContent3.isChecked = true
            }

            12 -> {
                binding.layoutAlarmTimeList.rbProfileAlarmContent4.isChecked = true
            }

            13 -> {
                binding.layoutAlarmTimeList.rbProfileAlarmContent5.isChecked = true
            }

            14 -> {
                binding.layoutAlarmTimeList.rbProfileAlarmContent6.isChecked = true
            }

            15 -> {
                binding.layoutAlarmTimeList.rbProfileAlarmContent7.isChecked = true
            }

            16 -> {
                binding.layoutAlarmTimeList.rbProfileAlarmContent8.isChecked = true
            }

            17 -> {
                binding.layoutAlarmTimeList.rbProfileAlarmContent9.isChecked = true
            }

            18 -> {
                binding.layoutAlarmTimeList.rbProfileAlarmContent10.isChecked = true
            }

            19 -> {
                binding.layoutAlarmTimeList.rbProfileAlarmContent11.isChecked = true
            }

            20 -> {
                binding.layoutAlarmTimeList.rbProfileAlarmContent12.isChecked = true
            }

            21 -> {
                binding.layoutAlarmTimeList.rbProfileAlarmContent13.isChecked = true
            }

            22 -> {
                binding.layoutAlarmTimeList.rbProfileAlarmContent14.isChecked = true
            }

            23 -> {
                binding.layoutAlarmTimeList.rbProfileAlarmContent15.isChecked = true
            }

            24 -> {
                binding.layoutAlarmTimeList.rbProfileAlarmContent16.isChecked = true
            }
        }
    }

}