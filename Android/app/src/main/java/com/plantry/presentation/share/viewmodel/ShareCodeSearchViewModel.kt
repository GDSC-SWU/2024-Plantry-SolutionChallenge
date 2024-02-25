package com.plantry.presentation.share.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.response.notification.ResponseNotificationCheckDto
import kotlinx.coroutines.launch

class ShareCodeSearchViewModel : ViewModel() {

    private val _shareCode: MutableLiveData<UiState<String>> = MutableLiveData()
    val shareCode: LiveData<UiState<String>> = _shareCode

    fun getShareCodeSearch(pantryId: Int) = viewModelScope.launch {
        runCatching {
            ApiPool.getShareCodeSearch.getShareCodeSearch(pantryId)
        }.fold({
            _shareCode.value = UiState.Success(it.data?.code ?: "")
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }
}