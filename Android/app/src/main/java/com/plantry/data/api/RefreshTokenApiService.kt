package com.plantry.data.api

import com.plantry.data.dto.BaseResponse
import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.RefreshTokenDto
import retrofit2.http.GET
import retrofit2.http.Header

interface RefreshTokenApiService : LogoutApiService {
    @GET("/api/v1/user/token")
    suspend fun getToken(
    ): BaseResponseNullable<RefreshTokenDto>
}