package com.plantry.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ResponseHomePantryDto(
    @SerialName("filter") val filter: String?,
    @SerialName("result") val result: List<Result?>?
) {
    @Serializable
    data class Result(
        @SerialName("day") val day: Int?,
        @SerialName("list") val list: List<Food>?
    ) {
        @Serializable
        data class Food(
            @SerialName("count") val count: Int?,
            @SerialName("days") val days: Int?,
            @SerialName("icon") val icon: String?,
            @SerialName("isNotified") val isNotified: Boolean?,
            @SerialName("isUseBydate") val isUseBydate: Boolean?,
            @SerialName("name") val name: String?
        )
    }
}