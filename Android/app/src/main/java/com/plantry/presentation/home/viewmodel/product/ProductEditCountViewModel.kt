package com.plantry.presentation.home.viewmodel.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import kotlinx.coroutines.launch

class ProductEditCountViewModel : ViewModel() {

    private val _productEditCount: MutableLiveData<UiState<Double>> = MutableLiveData()
    val productEditCount: LiveData<UiState<Double>> = _productEditCount

    fun patchEditCountProduct(product :Int, count : Double,) = viewModelScope.launch {
        runCatching {
            ApiPool.patchEditCountProduct.patchProductEditCount(product, count)
        }.fold({
            _productEditCount.value =
                UiState.Success(it.data?.count ?: -1.0)
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }
}