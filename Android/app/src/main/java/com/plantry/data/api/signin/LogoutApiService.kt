package com.plantry.data.api.signin

import com.plantry.data.dto.NullResponse
import retrofit2.http.DELETE

interface LogoutApiService {
    @DELETE("/api/v1/user/google")
    suspend fun deleteLogout(
    ): NullResponse
}