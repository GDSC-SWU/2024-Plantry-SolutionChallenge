package com.plantry.presentation.addfood.viewmodel.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.request.product.RequestProductAddMulti
import kotlinx.coroutines.launch

class ProductAddMultiViewModel : ViewModel() {

    private val _productAddMulti: MutableLiveData<UiState<Int?>> = MutableLiveData()
    val productAddMulti: LiveData<UiState<Int?>> = _productAddMulti

    fun postAddMultiProduct(requestProductAddDto: RequestProductAddMulti) = viewModelScope.launch {
        runCatching {
            ApiPool.postAddMultiProduct.postProductAddMulti(requestProductAddDto)
        }.fold({
            _productAddMulti.value =
                UiState.Success(it.data?.productId?: -1)
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }
}