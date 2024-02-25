package com.plantry.presentation.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import kotlinx.coroutines.launch


class ProfileAlarmTimeViewModel : ViewModel() {

    private val _alarmTime: MutableLiveData<UiState<Int>> = MutableLiveData()
    val alarmTime: LiveData<UiState<Int>> = _alarmTime

    init {
        getProfileAlarm()
    }

    fun getProfileAlarm() = viewModelScope.launch {
        runCatching {
            ApiPool.getProfileAlarm.getProfileAlarm()
        }.fold({
            _alarmTime.value =
                UiState.Success(it.data?.time ?: 0)
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }
}