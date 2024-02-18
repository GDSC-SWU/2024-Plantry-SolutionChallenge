package com.plantry.data.dto.response.product

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseProductAddDto(
    @SerialName("count")
    val count: Double?,
    @SerialName("date")
    val date: String?,
    @SerialName("icon")
    val icon: String?,
    @SerialName("isNotified")
    val isNotified: Boolean?,
    @SerialName("isUseByDate")
    val isUseByDate: Boolean?,
    @SerialName("name")
    val name: String?,
    @SerialName("pantryId")
    val pantryId: Int?,
    @SerialName("productId")
    val productId: Int?,
    @SerialName("storage")
    val storage: String?
)