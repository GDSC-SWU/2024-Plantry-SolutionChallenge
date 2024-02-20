package com.plantry.presentation.home.viewmodel.notification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.response.notification.ResponseNotificationCheckDto
import kotlinx.coroutines.launch

class NotificationConfirmViewModel : ViewModel() {

    private val _notificationConfirm: MutableLiveData<UiState<Int>> = MutableLiveData()
    val notificationConfirm: LiveData<UiState<Int>> = _notificationConfirm

    fun patchNotificationComfirm(id: Int) = viewModelScope.launch {
        runCatching {
            ApiPool.patchNotificationConfirm.patchNotificationConfirm(id)
        }.fold({
            _notificationConfirm.value = UiState.Success(it.data?.id ?: 0)
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }
}