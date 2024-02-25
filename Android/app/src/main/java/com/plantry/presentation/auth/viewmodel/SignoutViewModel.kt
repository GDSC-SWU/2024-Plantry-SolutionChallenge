package com.plantry.presentation.auth.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import kotlinx.coroutines.launch

class SignoutViewModel : ViewModel() {

    private val _signout: MutableLiveData<UiState<String>> = MutableLiveData()
    val signout: LiveData<UiState<String>> = _signout

    fun deleteGoogleSignout(num: Int) = viewModelScope.launch {
        runCatching {
            ApiPool.deleteSignOut.deleteSignout(num)
        }.fold(
            {
                _signout.value = UiState.Success(it.message)
            },
            {
                Log.d("AAA123", it.message.toString())
            }
        )
    }


}