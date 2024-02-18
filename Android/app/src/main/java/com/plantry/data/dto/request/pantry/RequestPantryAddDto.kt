package com.plantry.data.dto.request.pantry

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestPantryAddDto(
    @SerialName("title")
    val title: String?,
    @SerialName("color")
    val color: String?
)
