package com.plantry.data.dto.response.product

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ResponseProductIconListDto(
    @SerialName("group_name") val groupName: String,
    @SerialName("list") val foodList: List<Food>,
) {
    @Serializable
    data class Food(
        @SerialName("icon") val icon: String,
        @SerialName("name") val name: String,
    )
}