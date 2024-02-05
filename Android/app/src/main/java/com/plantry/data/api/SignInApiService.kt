package com.plantry.data.api

import com.plantry.data.dto.BaseResponse
import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.ResponseSignInDto
import retrofit2.http.GET
import retrofit2.http.Header

interface SignInApiService {
    @GET("/api/v1/user/google")
    suspend fun getSignIn(
        @Header("id-token") idToken : String,
        @Header("device-token")  deviceToken : String
    ): BaseResponseNullable<ResponseSignInDto>
}