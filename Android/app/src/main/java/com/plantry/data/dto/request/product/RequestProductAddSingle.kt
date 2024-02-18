package com.plantry.data.dto.request.product

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestProductAddSingle(
    @SerialName("count")
    val count: Double?,
    @SerialName("date")
    val date: String?,
    @SerialName("icon")
    val icon: String?,
    @SerialName("isUseByDate")
    val isUseByDate: Boolean?,
    @SerialName("name")
    val name: String?,
    @SerialName("pantry")
    val pantry: Int?,
    @SerialName("storage")
    val storage: String?
)