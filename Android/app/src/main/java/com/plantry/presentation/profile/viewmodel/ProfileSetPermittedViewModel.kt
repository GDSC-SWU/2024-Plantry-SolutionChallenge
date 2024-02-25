package com.plantry.presentation.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.response.profile.ResponseProfilePermittedSearchAlarmDto
import kotlinx.coroutines.launch

class ProfileSetPermittedViewModel : ViewModel() {

    private val _alarmPermitted: MutableLiveData<UiState<Boolean>> = MutableLiveData()
    val alarmPermitted: LiveData<UiState<Boolean>> = _alarmPermitted

    fun patchProfilePermittedSet() = viewModelScope.launch {
        runCatching {
            ApiPool.patchProfilePermittedSet.patchProfilePermittedSet()
        }.fold({
            _alarmPermitted.value =
                UiState.Success(it.data?.isPermitted ?: true)
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }
}