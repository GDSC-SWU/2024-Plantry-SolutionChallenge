package com.plantry.data.dto.response.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseNortificationAllListDto(
    @SerialName("result")
    val result: List<Result?>?
) {
    @Serializable
    data class Result(
        @SerialName("body")
        val body: String?,
        @SerialName("id")
        val id: Int?,
        @SerialName("isChecked")
        val isChecked: Boolean?,
        @SerialName("notifiedAt")
        val notifiedAt: String?,
        @SerialName("title")
        val title: String?,
        @SerialName("type")
        val type: String?
    )
}