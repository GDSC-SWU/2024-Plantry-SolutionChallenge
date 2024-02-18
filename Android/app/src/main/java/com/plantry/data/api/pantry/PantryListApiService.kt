package com.plantry.data.api.pantry

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.pantry.ResponsePantryDto
import retrofit2.http.GET

interface PantryListApiService {
    @GET("/api/v1/pantry")
    suspend fun getPantryList(
    ): BaseResponseNullable<ResponsePantryDto>
}