package com.plantry.data.api.pantry

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.request.pantry.RequestPantryAddDto
import com.plantry.data.dto.response.pantry.ResponsePantryAddDto
import retrofit2.http.Body
import retrofit2.http.POST


interface PantryAddApiService {
    @POST("/api/v1/pantry")
    suspend fun postAddPantry(
        @Body pantryAdd : RequestPantryAddDto
    ): BaseResponseNullable<ResponsePantryAddDto>
}