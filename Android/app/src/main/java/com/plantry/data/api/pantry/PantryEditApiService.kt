package com.plantry.data.api.pantry

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.request.pantry.RequestPantryAddDto
import com.plantry.data.dto.response.pantry.ResponsePantryAddDto
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Query

interface PantryEditApiService {
    @PATCH("/api/v1/pantry")
    suspend fun patchEditPantry(
        @Query("id") id : Int,
        @Body pantryAdd : RequestPantryAddDto
    ): BaseResponseNullable<ResponsePantryAddDto>
}