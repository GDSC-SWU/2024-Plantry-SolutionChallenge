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

class ProfileMissionSuccessViewModel : ViewModel() {

    private val _missionSuccessItem: MutableLiveData<UiState<Int>> = MutableLiveData()
    val missionSuccessItem: LiveData<UiState<Int>> = _missionSuccessItem


    fun patchMissionSuccessProfile(id :Int) = viewModelScope.launch {
        runCatching {
            ApiPool.patchMissionSuccessProfile.patchMissionSuccess(id)
        }.fold({
            _missionSuccessItem.value =
                UiState.Success(
                    it.data?.missionId ?: 0
                )
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }
}