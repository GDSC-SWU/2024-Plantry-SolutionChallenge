package com.plantry.data.dto.response.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseNoficationProductListDto (
    @SerialName("result")
    val result: List<Int?>?
)