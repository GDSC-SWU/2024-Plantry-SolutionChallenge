package com.plantry.presentation.share.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.response.notification.ResponseNotificationCheckDto
import com.plantry.data.dto.response.share.ResponseShareMemberDto
import kotlinx.coroutines.launch

class ShareMemberViewModel : ViewModel() {

    private val _shareMember: MutableLiveData<UiState<ResponseShareMemberDto>> = MutableLiveData()
    val shareMember: LiveData<UiState<ResponseShareMemberDto>> = _shareMember

    fun getShareCodeMember(pantryId: Int) = viewModelScope.launch {
        runCatching {
            ApiPool.getShareCodeMember.getShareCodeMember(pantryId)
        }.fold({
            _shareMember.value = UiState.Success(it.data ?:
            ResponseShareMemberDto(
                isUserOwner = null,
                list = null
            ))
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }
}