package com.plantry.presentation.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import kotlinx.coroutines.launch

class LogoutViewModel : ViewModel() {
    private val viewModel = RefreshTokenViewModel()

    private val _logout: MutableLiveData<UiState<String>> = MutableLiveData()
    val logout: LiveData<UiState<String>> = _logout

    fun deleteGoogleLogout() = viewModelScope.launch {
        runCatching {
            ApiPool.deleteLogOut.deleteLogout()
        }.fold(
            {
                _logout.value = UiState.Success(it.message)
            },
            {
            }
        )
    }


}