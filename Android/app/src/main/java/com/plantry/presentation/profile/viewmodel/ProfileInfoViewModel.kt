package com.plantry.presentation.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.response.profile.ResponseProfileInfoDto
import com.plantry.data.dto.response.profile.ResponseProfileMissionListDto
import kotlinx.coroutines.launch

class ProfileInfoViewModel : ViewModel() {

    private val _infoItem: MutableLiveData<UiState<ResponseProfileInfoDto>> =
        MutableLiveData()
    val infoItem: LiveData<UiState<ResponseProfileInfoDto>> = _infoItem


    fun getInfoProfile() = viewModelScope.launch {
        runCatching {
            ApiPool.getInfoProfile.getProfileInfo()
        }.fold({
            _infoItem.value =
                UiState.Success(
                    it.data?: ResponseProfileInfoDto(
                        email = "",
                        nickname = "",
                        profileImagePath = ""
                    )
                )
            Log.d("Aaa123", it.data.toString())
        }, {
            Log.d("Aaa12344", it.message.toString())
        })
    }
}