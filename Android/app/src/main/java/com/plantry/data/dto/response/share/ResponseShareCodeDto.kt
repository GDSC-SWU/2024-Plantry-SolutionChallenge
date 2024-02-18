package com.plantry.data.dto.response.share

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseShareCodeDto (
    @SerialName("code")
    val code: String?,
    @SerialName("pantryId")
    val pantryId: Int?
)