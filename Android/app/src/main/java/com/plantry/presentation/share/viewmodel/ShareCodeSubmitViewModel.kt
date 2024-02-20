package com.plantry.presentation.share.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.response.notification.ResponseNotificationCheckDto
import com.plantry.data.dto.response.share.ResponseShareCodeSubmitDto
import kotlinx.coroutines.launch

class ShareCodeSubmitViewModel : ViewModel() {

    private val _shareCodeSubmit: MutableLiveData<UiState<ResponseShareCodeSubmitDto>> = MutableLiveData()
    val shareCodeSubmit: LiveData<UiState<ResponseShareCodeSubmitDto>> = _shareCodeSubmit

    fun postShareCodeSubmit(code: Int) = viewModelScope.launch {
        runCatching {
            ApiPool.postShareCodeSubmit.postShareCodeSubmit(code)
        }.fold({
            _shareCodeSubmit.value = UiState.Success(it.data ?:
            ResponseShareCodeSubmitDto(
                color = null,
                pantryId = null,
                title = null
            ))
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }
}