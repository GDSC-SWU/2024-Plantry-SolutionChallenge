package com.plantry.presentation.addfood.viewmodel.ocr

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.dto.response.ocr.ResponseOcrSubmit
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class OcrSubmitViewModel : ViewModel() {

    private val _ocrResult: MutableLiveData<UiState<ResponseOcrSubmit>> =
        MutableLiveData()
    val ocrResult: LiveData<UiState<ResponseOcrSubmit>> = _ocrResult

    fun postOcrSubmit(file: MultipartBody.Part) = viewModelScope.launch {
        runCatching {
            // AIApiPool.postOcrSubmit.postOcrSubmit(file)
        }.fold({
            Handler(Looper.getMainLooper()).postDelayed({
            _ocrResult.value = UiState.Success(
                ResponseOcrSubmit(data = listOf(
                    ResponseOcrSubmit.Product(
                        food_name = "불고기 조각피자.",
                        quantity = 1,
                    ),
                    ResponseOcrSubmit.Product(
                        food_name = "소프트아이스크림",
                        quantity = 1,
                    ),
                    ResponseOcrSubmit.Product(
                        food_name = "닭반마리쌀국수",
                        quantity = 1,
                    ),
                )
            ))
            }, 3000)
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }


    fun setFaliureResult(){
        _ocrResult.value = UiState.Failure("")
    }
}