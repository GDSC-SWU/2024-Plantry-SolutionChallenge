package com.plantry.data.api.notification

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.request.notification.RequestNotificationProductEditDto
import com.plantry.data.dto.response.notification.ResponseNoficationProductListDto
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Query

interface NotificationEditApiService {
    @PATCH("/api/v1/notif/product")
    suspend fun patchNotificationEdit(
        @Query("product") product: Int,
        @Body notificationProductEdit : RequestNotificationProductEditDto
    ): BaseResponseNullable<ResponseNoficationProductListDto>
}