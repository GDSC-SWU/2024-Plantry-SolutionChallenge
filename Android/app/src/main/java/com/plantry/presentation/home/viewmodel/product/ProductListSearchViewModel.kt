package com.plantry.presentation.home.viewmodel.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.response.product.ResponseProductListDto
import kotlinx.coroutines.launch

class ProductListSearchViewModel : ViewModel() {

    private val _productListSearch: MutableLiveData<UiState<ResponseProductListDto>> = MutableLiveData()
    val productListSearch: LiveData<UiState<ResponseProductListDto>> = _productListSearch

    val exampleData = ResponseProductListDto(
        filter = "",
        result = listOf(
            ResponseProductListDto.Result(
                day = 1,
                list = listOf(
                    ResponseProductListDto.Result.Food(
                        productId = -1,
                        count = 5.0,
                        days = 1,
                        icon = "🍉",
                        isNotified = true,
                        isUseBydate = false,
                        name = "Example Product"
                    )
                )
            )
        )
    )

    fun getListSearchProduct(pantry: Int, filter: String) = viewModelScope.launch {
        runCatching {
            ApiPool.getListSearchProduct.getProductList(pantry, filter)
        }.fold({
            _productListSearch.value =
                UiState.Success(it.data ?:exampleData)
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }

}