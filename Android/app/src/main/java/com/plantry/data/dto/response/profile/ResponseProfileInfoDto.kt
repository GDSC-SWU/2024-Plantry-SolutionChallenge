package com.plantry.data.dto.response.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfileInfoDto(
    @SerialName("email")
    val email: String?,
    @SerialName("nickname")
    val nickname: String?,
    @SerialName("profileImagePath")
    val profileImagePath: String?
)
