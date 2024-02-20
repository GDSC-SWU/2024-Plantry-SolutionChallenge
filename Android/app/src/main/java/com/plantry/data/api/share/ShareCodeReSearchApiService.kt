package com.plantry.data.api.share

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.share.ResponseShareCodeDto
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface ShareCodeReSearchApiService {
    @PATCH("/api/v1/pantry/share/code")
    suspend fun patchShareCodeResearch(
        @Query("pantry") pantry: Int
    ): BaseResponseNullable<ResponseShareCodeDto>
}