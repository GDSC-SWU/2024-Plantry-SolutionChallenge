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

class ProfileMissionListViewModel : ViewModel() {

    private val _missionItem: MutableLiveData<UiState<List<ResponseProfileMissionListDto.Result?>>> =
        MutableLiveData()
    val missionItem: LiveData<UiState<List<ResponseProfileMissionListDto.Result?>>> = _missionItem


    init {
        getMissionListProfile()
    }

    fun getMissionListProfile() = viewModelScope.launch {
        runCatching {
            ApiPool.getMissionListProfile.getMissionList()
        }.fold({
            _missionItem.value =
                UiState.Success(
                    it.data?.result ?: listOf(
                        ResponseProfileMissionListDto.Result(
                            content = null,
                            isAchieved = false,
                            missionId = -1
                        )
                    )
                )
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }
}