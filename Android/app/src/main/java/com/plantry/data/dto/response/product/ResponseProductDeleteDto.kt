package com.plantry.data.dto.response.product

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseProductDeleteDto(
    @SerialName("id")
    val id: Int?,
    @SerialName("result")
    val result: Double?,
    @SerialName("type")
    val type: String?
)