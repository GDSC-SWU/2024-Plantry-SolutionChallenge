package com.plantry.data.api.pantry

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.pantry.ResponsePantryStarDto
import retrofit2.http.PATCH
import retrofit2.http.Query

interface PantryStarApiService {
    @PATCH("/api/v1/pantry/mark")
    suspend fun patchSetStar(
        @Query("id") id : Int,
    ): BaseResponseNullable<ResponsePantryStarDto>
}