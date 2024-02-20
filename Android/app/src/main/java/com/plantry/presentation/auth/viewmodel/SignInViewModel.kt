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

class SignInViewModel : ViewModel() {

    private val _accessToken: MutableLiveData<UiState<String>> = MutableLiveData()
    val accessToken: LiveData<UiState<String>> = _accessToken

    fun getGoogleLogin(idToken: String, deviceToken: String) = viewModelScope.launch {
        runCatching {
            ApiPool.getSignIn.getSignIn(idToken, deviceToken)
        }.fold(
            {
                _accessToken.value = UiState.Success(it.data?.accessToken ?: "")
                RetrofitPool.setAccessToken(it.data?.accessToken)
                RetrofitPool.setRefreshToken(it.data?.refreshToken)
                RetrofitPool.setUserId(it.data?.userId)
            },
            {
                Log.d("Aaa23", it.message.toString())
            }
        )
    }
}