package com.plantry.presentation.home.viewmodel.pantry

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.response.pantry.ResponsePantryDto
import kotlinx.coroutines.launch

class PantryListViewModel : ViewModel() {

    private val _pantryList: MutableLiveData<UiState<ResponsePantryDto>> = MutableLiveData()
    val pantryList: LiveData<UiState<ResponsePantryDto>> = _pantryList
    val list = arrayListOf(ResponsePantryDto.Result("", -1, false, "err"))


    fun getPantryList() = viewModelScope.launch {
        runCatching {
            ApiPool.getPantryList.getPantryList()
        }.fold({
            _pantryList.value =
                UiState.Success(it.data ?: ResponsePantryDto(list))
        }, {
            Log.d("Aaa133", it.message.toString())
        })
    }
}