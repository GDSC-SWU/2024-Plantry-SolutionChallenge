package com.plantry.presentation.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.RetrofitPool
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch

class SignInViewModel constructor(
    private val applicationContext: Context
): ViewModel() {

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
                // 401 구분 필요
                Toast.makeText(applicationContext, "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show()
            }
        )
    }

    companion object {
        val ID_MISSING = "Id token required."
        val DEVICE_MISSING = "Device token required."
        val LOGIN_FAILED = "Login failed."
        val INVAILD_ID = "Invalid ID token."
    }
}