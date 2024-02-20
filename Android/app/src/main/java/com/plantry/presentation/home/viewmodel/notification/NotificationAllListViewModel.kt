package com.plantry.presentation.home.viewmodel.notification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.response.notification.ResponseNortificationAllListDto
import kotlinx.coroutines.launch

class NotificationAllListViewModel : ViewModel() {

    private val _notificationAllList: MutableLiveData<UiState<ResponseNortificationAllListDto>> = MutableLiveData()
    val notificationAllList: LiveData<UiState<ResponseNortificationAllListDto>> = _notificationAllList

    fun getNotificationLAllList() = viewModelScope.launch {
        runCatching {
            ApiPool.getNotificationLAllList.getNotificationLAllList()
        }.fold({
            _notificationAllList.value = UiState.Success(it.data?: ResponseNortificationAllListDto(
                listOf(
                    ResponseNortificationAllListDto.Result(
                        body = "",
                        id = 0,
                        isChecked = null,
                        notifiedAt = "",
                        title = "",
                        type = ""
                    )
                )
            )
            )
        }, {
            Log.d("Aaa55", it.message.toString())
        })
    }
}