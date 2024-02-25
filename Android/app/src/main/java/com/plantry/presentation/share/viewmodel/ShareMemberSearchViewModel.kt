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

class ShareMemberSearchViewModel : ViewModel() {

    private val _shareMemberSearch: MutableLiveData<UiState<ResponseShareMemberDto>> = MutableLiveData()
    val shareMemberSearch: LiveData<UiState<ResponseShareMemberDto>> = _shareMemberSearch

    fun getShareMemberSearch(pantryId: Int, query: String) = viewModelScope.launch {
        runCatching {
            ApiPool.getShareMemberSearch.getShareMemberSearch(pantryId, query)
        }.fold({
            _shareMemberSearch.value = UiState.Success(it.data ?:
            ResponseShareMemberDto(
                isUserOwner = null,
                list = null
            ))
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }
}