package com.plantry.presentation.share.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.response.share.ResponseShareDeleteMemberDto
import kotlinx.coroutines.launch

class ShareMemberDeleteViewModel : ViewModel() {

    private val _shareMemberDelete: MutableLiveData<UiState<ResponseShareDeleteMemberDto>> = MutableLiveData()
    val shareMemberDelete: LiveData<UiState<ResponseShareDeleteMemberDto>> = _shareMemberDelete

    fun deleteShareMember(pantryId: Int, userId : Int) = viewModelScope.launch {
        runCatching {
            ApiPool.deleteShareMember.deleteShareMember(pantryId, userId)
        }.fold({
            _shareMemberDelete.value = UiState.Success(it.data ?:ResponseShareDeleteMemberDto(
                pantryId = null,
                userId = null
            ))
        }, {
            Log.d("Aaa13", it.message.toString())
            _shareMemberDelete.value = UiState.Failure(it.message.toString())
        })
    }
}