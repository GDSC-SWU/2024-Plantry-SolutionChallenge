package com.plantry.data.api.notification

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.notification.ResponseNotificationCheckDto
import retrofit2.http.PATCH
import retrofit2.http.Query

interface NotificationConfirmApiService {
    @PATCH("/api/v1/notif")
    suspend fun patchNotificationConfirm(
        @Query("id") alramId: Int,
    ): BaseResponseNullable<ResponseNotificationCheckDto>
}