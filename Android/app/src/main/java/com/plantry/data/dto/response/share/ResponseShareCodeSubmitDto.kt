package com.plantry.data.dto.response.share

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseShareCodeSubmitDto(
    @SerialName("color")
    val color: String?,
    @SerialName("pantryId")
    val pantryId: Int?,
    @SerialName("title")
    val title: String?
)