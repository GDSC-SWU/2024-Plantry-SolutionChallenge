package com.plantry.data.api.notification

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.notification.ResponseNortificationAllListDto
import retrofit2.http.GET

interface NotificationAllListApiService {
    @GET("/api/v1/notif")
    suspend fun getNotificationLAllList(
    ): BaseResponseNullable<ResponseNortificationAllListDto>
}