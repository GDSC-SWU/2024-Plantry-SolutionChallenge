package com.plantry.data.dto.response.signin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseSignInDto(
    @SerialName("accessToken") val accessToken:  String,
    @SerialName("nickname") val nickname: String,
    @SerialName("refreshToken") val refreshToken: String,
    @SerialName("userId") val userId: Int
)