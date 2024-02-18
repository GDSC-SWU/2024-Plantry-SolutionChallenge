package com.plantry.data.dto.response.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ResponseProfileNameChangeDto (
    @SerialName("nickname")
    val nickname: String?,
)
