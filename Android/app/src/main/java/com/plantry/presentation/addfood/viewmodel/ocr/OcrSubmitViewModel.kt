package com.plantry.presentation.addfood.viewmodel.ocr

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantry.coreui.view.UiState
import com.plantry.data.ApiPool
import com.plantry.data.dto.response.notification.ResponseNoficationProductListDto
import com.plantry.data.dto.response.ocr.ResponseOcrSubmit
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class OcrSubmitViewModel : ViewModel() {

    private val _ocrResult: MutableLiveData<UiState<ResponseOcrSubmit>> =
        MutableLiveData()
    val ocrResult: LiveData<UiState<ResponseOcrSubmit>> = _ocrResult

    fun postOcrSubmit(file: MultipartBody.Part) = viewModelScope.launch {
        runCatching {
            ApiPool.postOcrSubmit.postOcrSubmit(file)
        }.fold({
            _ocrResult.value = UiState.Success(
                it
            )
        }, {
            Log.d("Aaa13", it.message.toString())
        })
    }
}