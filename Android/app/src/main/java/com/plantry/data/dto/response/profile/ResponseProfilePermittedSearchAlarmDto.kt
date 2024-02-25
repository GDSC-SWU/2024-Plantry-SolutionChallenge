package com.plantry.data.dto.response.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfilePermittedSearchAlarmDto(
    @SerialName("isPermitted")
    val isPermitted: Boolean
)