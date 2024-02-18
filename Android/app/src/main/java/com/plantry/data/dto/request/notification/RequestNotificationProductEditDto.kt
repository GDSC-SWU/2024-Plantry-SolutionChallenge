package com.plantry.data.dto.request.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestNotificationProductEditDto (
    @SerialName("list")
    val list: List<Int?>?
)