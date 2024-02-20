package com.plantry.presentation.home.ui

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.plantry.R
import com.plantry.coreui.adapter.ItemClick
import com.plantry.coreui.base.BindingFragment
import com.plantry.coreui.view.UiState
import com.plantry.data.dto.response.notification.ResponseNortificationAllListDto
import com.plantry.databinding.FragmentHomeNotificationBinding
import com.plantry.presentation.home.adapter.notification.HomeNotificationAdapter
import com.plantry.presentation.home.viewmodel.notification.NotificationAllListViewModel
import com.plantry.presentation.home.viewmodel.notification.NotificationConfirmViewModel

class FragmentHomeNotification :
    BindingFragment<FragmentHomeNotificationBinding>(R.layout.fragment_home_notification) {

    override var bnvVisibility = View.GONE

    private val viewModeAlarmlList by viewModels<NotificationAllListViewModel>()
    private val viewModeAlarmConfirm by viewModels<NotificationConfirmViewModel>()


    override fun initView() {
        clickBackStack()
        observeNotiList()
        observeNoriConfirm()
    }


    private fun clickBackStack() {
        binding.ivHomeNotificationToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun clickItem(adapter: HomeNotificationAdapter, item: ResponseNortificationAllListDto) {
        adapter.notificationClick = object : ItemClick {
            override fun onClick(view: View, position: Int) {
                when (view.id) {
                    R.id.cl_notification_item_layout -> {
                        if(item.result?.get(position)?.isChecked != true){
                            viewModeAlarmConfirm.patchNotificationComfirm(item.result?.get(position)?.id ?: 0)
                        }
                        findNavController().navigate(R.id.action_notification_to_home)
                    }
                }
            }
        }
    }

    private fun observeNoriConfirm(){
        viewModeAlarmConfirm.notificationConfirm.observe(this) {
            when (it) {
                is UiState.Success -> {
                    Log.d("aaa", it.data.toString())
                }

                else -> Unit
            }
        }
    }

    private fun observeNotiList() {
        viewModeAlarmlList.notificationAllList.observe(this) {
            when (it) {
                is UiState.Success -> {
                    val adapter = HomeNotificationAdapter()
                    binding.rcvNotificationList.adapter = adapter
                    val pantryList = it.data.result
                    adapter.submitList(pantryList)
                    clickItem(adapter, it.data)
                    if(it.data.result.isNullOrEmpty()){
                        binding.layoutHomeNotificationEmptyView.clHomeNotiEmptyView.visibility = View.VISIBLE
                    }
                }

                else -> Unit
            }
        }
    }

}