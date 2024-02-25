package com.plantry.presentation.home.viewmodel.pantry

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.request.pantry.RequestPantryAddDto
import com.plantry.data.dto.response.pantry.ResponsePantryAddDto
import kotlinx.coroutines.launch

class PantryEditViewModel : ViewModel() {

    private val _pantryEdit: MutableLiveData<UiState<ResponsePantryAddDto>> = MutableLiveData()
    val pantryEdit: LiveData<UiState<ResponsePantryAddDto>> = _pantryEdit


    fun patchEditPantry(id: Int, pantryEdit: RequestPantryAddDto) = viewModelScope.launch {
        runCatching {
            ApiPool.patchEditPantry.patchEditPantry(id, pantryEdit)
        }.fold({
            _pantryEdit.value =
                UiState.Success(it.data ?: ResponsePantryAddDto("",-1,""))
        }, {
            Log.d("Aaa123", it.message.toString())
        })
    }
}