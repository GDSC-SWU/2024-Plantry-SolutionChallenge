package com.plantry.presentation.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.response.profile.ResponseProfileMissionListDto
import kotlinx.coroutines.launch

class ProfileNameChangeViewModel : ViewModel() {

    private val _nameItem: MutableLiveData<UiState<String>> = MutableLiveData()
    val nameItem: LiveData<UiState<String>> = _nameItem


    fun patchNameChangeProfile(nickname: String) = viewModelScope.launch {
        runCatching {
            ApiPool.patchNameChangeProfile.patchProfileNameChange(nickname)
        }.fold({
            _nameItem.value =
                UiState.Success(it.data?.nickname ?: "")
        }, {
            Log.d("Aaa13", it.message.toString())
            _nameItem.value = UiState.Failure(it.message)
        })
    }
}