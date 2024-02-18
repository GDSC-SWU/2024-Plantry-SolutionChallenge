package com.plantry.data.dto.response.pantry

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ResponsePantryDto(
    @SerialName ("result")
    val result: List<Result>
) {
    @Serializable
    data class Result(
        @SerialName("color")
        val color: String?,
        @SerialName ("id")
        val id: Int,
        @SerialName ("isMarked")
        val isMarked: Boolean,
        @SerialName ("title")
        val title: String?
    )
}