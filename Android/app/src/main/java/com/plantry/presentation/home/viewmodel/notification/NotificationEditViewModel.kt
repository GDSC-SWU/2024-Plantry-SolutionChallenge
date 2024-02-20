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

class NotificationEditViewModel : ViewModel() {

    private val _notificationEdit: MutableLiveData<UiState<ResponseNoficationProductListDto>> =
        MutableLiveData()
    val notificationEdit: LiveData<UiState<ResponseNoficationProductListDto>> = _notificationEdit

    fun patchNotificationEdit(
        product: Int,
        notificationProductEdit: RequestNotificationProductEditDto
    ) = viewModelScope.launch {
        runCatching {
            ApiPool.patchNotificationEdit.patchNotificationEdit(product, notificationProductEdit)
        }.fold({
            _notificationEdit.value = UiState.Success(
                it.data ?: ResponseNoficationProductListDto(
                    listOf(0)
                )
            )
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }
}