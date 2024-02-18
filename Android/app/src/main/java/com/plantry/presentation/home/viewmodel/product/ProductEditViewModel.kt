package com.plantry.presentation.home.viewmodel.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.request.pantry.RequestPantryAddDto
import com.plantry.data.dto.request.product.RequestProductAddSingle
import kotlinx.coroutines.launch

class ProductEditViewModel : ViewModel() {

    private val _productEdit: MutableLiveData<UiState<Int>> = MutableLiveData()
    val productEdit: LiveData<UiState<Int>> = _productEdit


    fun patchEditProduct(productId: Int, productData : RequestProductAddSingle) = viewModelScope.launch {
        runCatching {
            ApiPool.patchEditProduct.patchProductEdit(productId, productData)
        }.fold({
            _productEdit.value =
                UiState.Success(it.data?.productId ?: -1)
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }
}