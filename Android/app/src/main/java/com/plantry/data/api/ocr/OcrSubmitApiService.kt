package com.plantry.data.api.ocr

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.notification.ResponseNoficationProductListDto
import com.plantry.data.dto.response.ocr.ResponseOcrSubmit
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface OcrSubmitApiService {
    @Multipart
    @POST("/api/v1/parse-receipt")
    suspend fun postOcrSubmit(
        @Part file: MultipartBody.Part,
    ): ResponseOcrSubmit
}