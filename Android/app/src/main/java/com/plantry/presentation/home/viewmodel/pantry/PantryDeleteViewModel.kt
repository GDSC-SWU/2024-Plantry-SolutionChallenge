package com.plantry.presentation.home.viewmodel.pantry

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import kotlinx.coroutines.launch

class PantryDeleteViewModel : ViewModel() {

    private val _pantryDelete: MutableLiveData<UiState<String>> = MutableLiveData()
    val pantryDelete: LiveData<UiState<String>> = _pantryDelete

    fun deletePantry(id: Int) = viewModelScope.launch {
        runCatching {
            ApiPool.deletePantry.deletePantry(id)
        }.fold({
            _pantryDelete.value = UiState.Success(it.message)
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }
}