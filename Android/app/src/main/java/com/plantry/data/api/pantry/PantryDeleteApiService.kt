package com.plantry.data.api.pantry

import com.plantry.data.dto.NullResponse
import retrofit2.http.DELETE
import retrofit2.http.Query

interface PantryDeleteApiService {
    @DELETE("/api/v1/pantry")
    suspend fun deletePantry(
        @Query("id") id : Int,
    ): NullResponse
}