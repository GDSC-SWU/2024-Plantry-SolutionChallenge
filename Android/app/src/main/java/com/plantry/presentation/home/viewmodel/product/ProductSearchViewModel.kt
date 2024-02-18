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

class ProductSearchViewModel : ViewModel() {

    private val _productSearch: MutableLiveData<UiState<ResponseProductListDto>> = MutableLiveData()
    val productSearch: LiveData<UiState<ResponseProductListDto>> = _productSearch

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

    fun getSearchProduct(pantry: Int, filter: String, query: String) = viewModelScope.launch {
        runCatching {
            ApiPool.getSearchProduct.getProductSearch(pantry, filter, query)
        }.fold({
            _productSearch.value =
                UiState.Success(it.data?: exampleData)
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }
}