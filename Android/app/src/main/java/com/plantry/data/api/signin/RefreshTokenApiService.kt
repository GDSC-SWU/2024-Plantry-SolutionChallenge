package com.plantry.data.api.signin

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.signin.RefreshTokenDto
import retrofit2.http.GET

interface RefreshTokenApiService : LogoutApiService {
    @GET("/api/v1/user/token")
    suspend fun getToken(
    ): BaseResponseNullable<RefreshTokenDto>
}