package com.plantry.data.api.signin

import com.plantry.data.dto.NullResponse
import retrofit2.http.DELETE
import retrofit2.http.Query

interface SignoutApiService {
    @DELETE("/api/v1/user")
    suspend fun deleteSignout(
        @Query("cause") cause : Int
    ): NullResponse
}