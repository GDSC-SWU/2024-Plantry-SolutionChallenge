package com.plantry.presentation.auth

import android.content.Context
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.MainActivity
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.RetrofitPool
import kotlinx.coroutines.launch

class LogoutViewModel: ViewModel() {
    private val viewModel  = RefreshTokenViewModel()

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