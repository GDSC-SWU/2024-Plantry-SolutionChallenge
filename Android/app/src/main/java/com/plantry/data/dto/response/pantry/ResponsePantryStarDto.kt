package com.plantry.data.dto.response.pantry

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePantryStarDto(
    @SerialName("id")
    val id: Int?,
    @SerialName("isMarked")
    val isMarked: Boolean?
)