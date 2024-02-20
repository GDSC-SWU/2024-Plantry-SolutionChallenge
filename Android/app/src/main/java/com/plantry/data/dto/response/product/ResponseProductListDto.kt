package com.plantry.data.dto.response.product

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ResponseProductListDto(
    @SerialName("filter") val filter: String?,
    @SerialName("result") val result: List<Result?>,
) {
    @Serializable
    data class Result(
        @SerialName("day") val day: Int?,
        @SerialName("list") val list: List<Food>?,
    ) {
        @Serializable
        data class Food(
            @SerialName("productId") val productId: Int?,
            @SerialName("count") val count: Double?,
            @SerialName("days") val days: Int?,
            @SerialName("icon") val icon: String?,
            @SerialName("isNotified") val isNotified: Boolean?,
            @SerialName("isUseByDate") val isUseBydate: Boolean?,
            @SerialName("name") val name: String?,
            @SerialName("storage") val storage: String?
        )
    }
}