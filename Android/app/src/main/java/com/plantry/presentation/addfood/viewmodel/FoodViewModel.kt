package com.plantry.presentation.addfood.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.plantry.coreui.view.UiState


class FoodViewModel : ViewModel() {
    private val _iconLiveData = MutableLiveData<UiState<String>>()
    val iconLiveData: LiveData<UiState<String>> = _iconLiveData

    private val _nameLiveData = MutableLiveData<UiState<String>>()
    val nameLiveData: LiveData<UiState<String>> = _nameLiveData

    fun setSucessIcon(icon: String) {
        _iconLiveData.value = UiState.Success(icon)
    }
    fun setFaliureIcon(){
        _iconLiveData.value = UiState.Failure("")
    }

    fun setSucessName(name: String) {
        _nameLiveData.value = UiState.Success(name)
    }
    fun setFaliureName(){
        _nameLiveData.value = UiState.Failure("")
    }
}
