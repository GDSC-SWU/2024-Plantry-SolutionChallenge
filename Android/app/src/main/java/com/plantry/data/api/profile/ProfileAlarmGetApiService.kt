package com.plantry.data.api.profile

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.profile.ResponseProfileAlarmChangeDto
import retrofit2.http.GET

interface ProfileAlarmGetApiService {
    @GET("/api/v1/mypage/notif")
    suspend fun getProfileAlarm(
    ): BaseResponseNullable<ResponseProfileAlarmChangeDto>
}