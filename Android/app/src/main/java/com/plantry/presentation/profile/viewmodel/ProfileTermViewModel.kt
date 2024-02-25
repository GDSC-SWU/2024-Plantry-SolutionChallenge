package com.plantry.presentation.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.response.profile.ResponseProfileGetTermDto
import kotlinx.coroutines.launch


class ProfileTermViewModel : ViewModel() {

    private val _termItem: MutableLiveData<UiState<ResponseProfileGetTermDto>> = MutableLiveData()
    val termItem: LiveData<UiState<ResponseProfileGetTermDto>> = _termItem


    fun getTermProfile(type: String) = viewModelScope.launch {
        runCatching {
            ApiPool.getTermProfile.getTerms(type)
        }.fold({
            _termItem.value =
                UiState.Success(
                    it.data ?: ResponseProfileGetTermDto(
                        type = "",
                        result = listOf(
                            ResponseProfileGetTermDto.Result(
                                content = null,
                                createdAt = null,
                                title = null,
                                updatedAt = ""
                            )
                        )
                    )
                )
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }

    companion object {
        const val ALL = "all"
        const val USE = "use"
        const val PRIVACY = "privacy"
    }
}