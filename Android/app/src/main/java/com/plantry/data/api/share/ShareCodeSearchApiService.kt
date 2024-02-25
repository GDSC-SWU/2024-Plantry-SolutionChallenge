package com.plantry.data.api.share

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.share.ResponseShareCodeDto
import retrofit2.http.GET
import retrofit2.http.Query


interface ShareCodeSearchApiService {
    @GET("/api/v1/pantry/share/code")
    suspend fun getShareCodeSearch(
        @Query("pantry") pantry: Int
    ): BaseResponseNullable<ResponseShareCodeDto>
}