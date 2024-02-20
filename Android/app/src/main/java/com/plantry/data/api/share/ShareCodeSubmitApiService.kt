package com.plantry.data.api.share

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.share.ResponseShareCodeDto
import com.plantry.data.dto.response.share.ResponseShareCodeSubmitDto
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface ShareCodeSubmitApiService {
    @POST("/api/v1/pantry/share/code")
    suspend fun postShareCodeSubmit(
        @Query("code") code: Int
    ): BaseResponseNullable<ResponseShareCodeSubmitDto>
}