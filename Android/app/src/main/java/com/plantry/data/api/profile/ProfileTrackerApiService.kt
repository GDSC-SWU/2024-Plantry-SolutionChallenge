package com.plantry.data.api.profile

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.profile.ResponseProfileTrackerDto
import retrofit2.http.GET

interface ProfileTrackerApiService {
    @GET("/api/v1/mypage/track")
    suspend fun getTracker(
    ): BaseResponseNullable<ResponseProfileTrackerDto>
}