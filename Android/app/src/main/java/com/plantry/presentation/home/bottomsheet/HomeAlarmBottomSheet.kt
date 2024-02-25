package com.plantry.presentation.home.bottomsheet

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.plantry.R
import com.plantry.coreui.base.BindingBottomSheetFragment
import com.plantry.coreui.fragment.longToast
import com.plantry.coreui.view.UiState
import com.plantry.data.dto.request.notification.RequestNotificationProductEditDto
import com.plantry.databinding.BottomsheetHomeAlarmBinding
import com.plantry.presentation.home.viewmodel.notification.NotificationEditViewModel
import com.plantry.presentation.home.viewmodel.notification.NotificationListViewModel
import com.plantry.presentation.home.viewmodel.product.ProductListSearchViewModel
import com.plantry.presentation.profile.viewmodel.ProfileAlarmTimeViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeAlarmBottomSheet :
    BindingBottomSheetFragment<BottomsheetHomeAlarmBinding>(R.layout.bottomsheet_home_alarm) {
    private val viewModelProductList by viewModels<ProductListSearchViewModel>({ requireParentFragment() })
    private val viewModelNotiList by viewModels<NotificationListViewModel>()
    private val viewModelNotiEdit by viewModels<NotificationEditViewModel>()
    private val viewModelAlarmTime by viewModels<ProfileAlarmTimeViewModel>()

    private var list: MutableList<Int?>? = mutableListOf()
    private var alarmState : Boolean = true

    override fun initView() {
        observeAlarmTime()
        setAlarmCheck()
        observeNotiList()
        clickAlarmSort()
        observeEdit()
        clickConfirmButton()
    }

    private fun setAlarmCheck() {
        val productId = arguments?.getInt("productId")
        if (productId != null) {
            viewModelNotiList.getNotificationList(productId)
        }
    }

    private fun clickAlarmSort() {
        binding.tvHomeBottomSheetAlarmDeadline.setOnClickListener {
            binding.tvHomeBottomSheetAlarmDeadline.isSelected = !binding.tvHomeBottomSheetAlarmDeadline.isSelected
            if (binding.tvHomeBottomSheetAlarmDeadline.isSelected) {
                list?.add(0)
            }

        }
        binding.tvHomeBottomSheetAlarmOne.setOnClickListener {
            binding.tvHomeBottomSheetAlarmOne.isSelected = !binding.tvHomeBottomSheetAlarmOne.isSelected
            if (binding.tvHomeBottomSheetAlarmOne.isSelected) {
                list?.add(1)
            }
        }
        binding.tvHomeBottomSheetAlarmThree.setOnClickListener {
            binding.tvHomeBottomSheetAlarmThree.isSelected = !binding.tvHomeBottomSheetAlarmThree.isSelected
            if (binding.tvHomeBottomSheetAlarmThree.isSelected) {
                list?.add(3)
            }
        }
        binding.tvHomeBottomSheetAlarmSeven.setOnClickListener {
            binding.tvHomeBottomSheetAlarmSeven.isSelected = !binding.tvHomeBottomSheetAlarmSeven.isSelected
            if (binding.tvHomeBottomSheetAlarmSeven.isSelected) {
                list?.add(7)
            }
        }
    }

    private fun clickConfirmButton() {
        binding.tvHomeBottomSheetAlarmConfirm.setOnClickListener {
            editNotiList()
            dismiss()
        }
    }

    private fun editNotiList() {
        val productId = arguments?.getInt("productId")
        if(alarmState){
            if (productId != null) {
                viewModelNotiEdit.patchNotificationEdit(productId,RequestNotificationProductEditDto(list))
                list = mutableListOf()
            }
        }
        else{
            longToast("Notification settings are not set. Please set notification settings first in notification.")
        }
    }

    private fun setCheckItem(num: Int) {
        when (num) {
            0 -> {
                binding.tvHomeBottomSheetAlarmDeadline.isSelected = true
            }

            1 -> {
                binding.tvHomeBottomSheetAlarmOne.isSelected = true
            }

            3 -> {
                binding.tvHomeBottomSheetAlarmThree.isSelected = true
            }

            7 -> {
                binding.tvHomeBottomSheetAlarmSeven.isSelected = true
            }
        }
    }

    private fun changeProductList() {
        val pantryId = arguments?.getInt("pantryId")
        val pantryFilter = arguments?.getString("pantryFilter")
        if (pantryId != null && pantryFilter != null) {
            viewModelProductList.getListSearchProduct(pantryId, pantryFilter)
        }
    }

    private fun observeEdit() {
        viewModelNotiEdit.notificationEdit.observe(this) {
            when (it) {
                is UiState.Success -> {
                    if (it.data.result != null) {
                        for (i in it.data.result) {
                            setCheckItem(i ?: 0)
                        }
                    }
                    changeProductList()
                }

                else -> Unit
            }
        }
    }

    private fun convertToHourFormat(time: Int): String {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, time)
        }
        if (time == 0) {
            calendar.set(Calendar.HOUR_OF_DAY, 9)
        }

        val dateFormat = SimpleDateFormat("h a", Locale.getDefault())
        val formattedTime = dateFormat.format(calendar.time).toString()
        val dotFormattedTime = formattedTime.replace("AM", "A.M").replace("PM", "P.M")

        return dotFormattedTime
    }

    private fun observeAlarmTime() {
        viewModelAlarmTime.alarmTime.observe(this) {
            when (it) {
                is UiState.Success -> {
                    alarmState = it.data != 0
                    binding.tvHomeBottomSheetAlarmDeadline.text =  "Alarm at Deadline (${convertToHourFormat(it.data)})"
                }

                else -> Unit
            }
        }
    }

    private fun observeNotiList() {
        viewModelNotiList.notificationList.observe(this) {
            when (it) {
                is UiState.Success -> {
                    if (it.data.result != null) {
                        for (i in it.data.result) {
                            setCheckItem(i ?: 0)
                        }
                    }
                    changeProductList()
                }

                else -> Unit
            }
        }
    }
}