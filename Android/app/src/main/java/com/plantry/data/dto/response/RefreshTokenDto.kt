package com.plantry.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenDto (
    @SerialName ("accessToken")
    val accessToken : String,
    @SerialName ("refreshToken")
    val refreshToken : String,
)