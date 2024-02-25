package com.plantry.data.dto.response.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfileAlarmChangeDto(
    @SerialName("time")
    val time: Int?
)