package com.plantry.presentation.auth.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.RetrofitPool
import kotlinx.coroutines.launch

class RefreshTokenViewModel: ViewModel() {

    private val _refreshToken: MutableLiveData<UiState<String>> = MutableLiveData()
    val refreshToken: LiveData<UiState<String>> = _refreshToken

    fun getRefreshToken() = viewModelScope.launch {
        runCatching {
            ApiPool.getRefreshToken.getToken()
        }.fold(
            {
                _refreshToken.value = UiState.Success(it.data?.accessToken ?: "")
                RetrofitPool.setAccessToken(it.data?.accessToken)
                RetrofitPool.setRefreshToken(it.data?.refreshToken)
            },
            {
                Log.d("Aaa1312", it.message.toString())
            }
        )
    }

}