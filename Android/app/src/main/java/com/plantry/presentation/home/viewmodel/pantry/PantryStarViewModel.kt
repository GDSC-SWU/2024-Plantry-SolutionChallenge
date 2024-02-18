package com.plantry.presentation.home.viewmodel.pantry

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.response.pantry.ResponsePantryStarDto
import kotlinx.coroutines.launch

class PantryStarViewModel : ViewModel() {

    private val _pantryStar: MutableLiveData<UiState<ResponsePantryStarDto>> = MutableLiveData()
    val pantryStar: LiveData<UiState<ResponsePantryStarDto>> = _pantryStar


    fun patchSetStar(id: Int) = viewModelScope.launch {
        runCatching {
            ApiPool.patchSetStar.patchSetStar(id)
        }.fold({
            _pantryStar.value =
                UiState.Success(it.data ?: ResponsePantryStarDto(-1,false))
        }, {
            Log.d("Aaa123", it.message.toString())
        })
    }
}