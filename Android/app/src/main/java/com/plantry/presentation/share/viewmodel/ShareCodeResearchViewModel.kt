package com.plantry.presentation.share.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.response.notification.ResponseNotificationCheckDto
import com.plantry.data.dto.response.share.ResponseShareCodeDto
import kotlinx.coroutines.launch

class ShareCodeResearchViewModel : ViewModel() {

    private val _shareCode: MutableLiveData<UiState<String>> = MutableLiveData()
    val shareCode: LiveData<UiState<String>> = _shareCode

    fun patchShareCodeResearch(pantryId: Int) = viewModelScope.launch {
        runCatching {
            ApiPool.patchShareCodeResearch.patchShareCodeResearch(pantryId)
        }.fold({
            _shareCode.value = UiState.Success(it.data?.code ?: "")
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }
}