package com.plantry.presentation.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import kotlinx.coroutines.launch


class ProfileAlarmViewModel : ViewModel() {

    private val _alarmItem: MutableLiveData<UiState<Int>> = MutableLiveData()
    val alarmItem: LiveData<UiState<Int>> = _alarmItem


    fun patchAlarmProfile(time: Int) = viewModelScope.launch {
        runCatching {
            ApiPool.patchAlarmProfile.patchProfileAlarmChange(time)
        }.fold({
            _alarmItem.value =
                UiState.Success(it.data?.time ?: -1)
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }
}