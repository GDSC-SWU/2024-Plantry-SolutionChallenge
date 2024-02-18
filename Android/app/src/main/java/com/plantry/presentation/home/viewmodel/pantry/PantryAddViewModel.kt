package com.plantry.presentation.home.viewmodel.pantry

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.request.pantry.RequestPantryAddDto
import kotlinx.coroutines.launch

class PantryAddViewModel : ViewModel() {

    private val _pantryItem: MutableLiveData<UiState<Int>> = MutableLiveData()
    val pantryItem: LiveData<UiState<Int>> = _pantryItem


    fun postAddPantry(pantryItem: RequestPantryAddDto) = viewModelScope.launch {
        runCatching {
            ApiPool.postAddPantry.postAddPantry(pantryItem)
        }.fold({
            _pantryItem.value =
                UiState.Success(it.data?.pantryId ?: -1)
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }
}