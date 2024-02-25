package com.plantry.presentation.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.response.profile.ResponseProfileTrackerDto
import kotlinx.coroutines.launch


class ProfileTrackerViewModel : ViewModel() {

    private val _trackerItem: MutableLiveData<UiState<ResponseProfileTrackerDto>> = MutableLiveData()
    val trackerItem: LiveData<UiState<ResponseProfileTrackerDto>> = _trackerItem


    fun getTrackerProfile() = viewModelScope.launch {
        runCatching {
            ApiPool.getTrakerProfile.getTracker()
        }.fold({
            _trackerItem.value =
                UiState.Success(
                    it.data ?: ResponseProfileTrackerDto(
                        Disposal = 0.0,
                        Ingestion = 0.0,
                        Mistake = 0.0,
                        Share = 0.0,
                    )
                )
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }

}