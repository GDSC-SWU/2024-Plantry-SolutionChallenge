package com.plantry.data.dto.response.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseNotificationCheckDto(
    @SerialName("id")
    val id: Int?
)