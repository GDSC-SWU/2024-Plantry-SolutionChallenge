package com.plantry.presentation.home.viewmodel.notification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.request.notification.RequestNotificationProductEditDto
import com.plantry.data.dto.response.notification.ResponseNoficationProductListDto
import kotlinx.coroutines.launch

class NotificationListViewModel : ViewModel() {

    private val _notificationList: MutableLiveData<UiState<ResponseNoficationProductListDto>> =
        MutableLiveData()
    val notificationList: LiveData<UiState<ResponseNoficationProductListDto>> = _notificationList

    fun getNotificationList(
        product: Int
    ) = viewModelScope.launch {
        runCatching {
            ApiPool.getNotificationList.getNotificationList(product)
        }.fold({
            _notificationList.value = UiState.Success(
                it.data ?: ResponseNoficationProductListDto(
                    listOf(0)
                )
            )
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }
}