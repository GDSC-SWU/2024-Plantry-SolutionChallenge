package com.plantry.data.api.notification

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.notification.ResponseNoficationProductListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationListApiService {
    @GET("/api/v1/notif/product")
    suspend fun getNotificationList(
        @Query("product") product: Int
    ): BaseResponseNullable<ResponseNoficationProductListDto>
}