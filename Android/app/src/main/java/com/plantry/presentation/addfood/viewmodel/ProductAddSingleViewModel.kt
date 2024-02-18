package com.plantry.presentation.addfood.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.request.product.RequestProductAddSingle
import kotlinx.coroutines.launch

class ProductAddSingleViewModel : ViewModel() {

    private val _productAddSingle: MutableLiveData<UiState<Int>> = MutableLiveData()
    val productAddSingle: LiveData<UiState<Int>> = _productAddSingle


    fun postAddSingleProduct(requestProductAddDto: RequestProductAddSingle) = viewModelScope.launch {
        runCatching {
            ApiPool.postAddSingleProduct.postProductAddSingle(requestProductAddDto)
        }.fold({
            _productAddSingle.value =
                UiState.Success(it.data?.productId ?: -1)
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }
}