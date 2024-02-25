package com.plantry.data.dto.response.share

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseShareDeleteMemberDto(
    @SerialName("pantryId")
    val pantryId: Int?,
    @SerialName("userId")
    val userId: Int?
)