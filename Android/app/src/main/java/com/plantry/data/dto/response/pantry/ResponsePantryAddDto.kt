package com.plantry.data.dto.response.pantry

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePantryAddDto(
    @SerialName("color")
    val color: String?,
    @SerialName("pantryId")
    val pantryId: Int?,
    @SerialName("title")
    val title: String?
)