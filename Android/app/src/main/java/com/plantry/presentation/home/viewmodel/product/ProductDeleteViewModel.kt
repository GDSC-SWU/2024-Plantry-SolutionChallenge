package com.plantry.presentation.home.viewmodel.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.request.pantry.RequestPantryAddDto
import kotlinx.coroutines.launch

class ProductDeleteViewModel : ViewModel() {

    private val _productDelete: MutableLiveData<UiState<Int>> = MutableLiveData()
    val productDelete: LiveData<UiState<Int>> = _productDelete


    fun deleteProduct(product: Int, type: Int, count: Double) = viewModelScope.launch {
        runCatching {
            ApiPool.deleteProduct.deleteProduct(product, type, count)
        }.fold({
            _productDelete.value = UiState.Success(it.data?.id ?: -1)
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }

    fun setProductDeleteEmpty(){
        _productDelete.value = UiState.Empty
    }
}