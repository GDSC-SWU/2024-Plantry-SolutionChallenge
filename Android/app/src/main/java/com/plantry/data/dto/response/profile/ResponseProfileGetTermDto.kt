package com.plantry.data.dto.response.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfileGetTermDto(
    @SerialName("result")
    val result: List<Result?>?,
    @SerialName("type")
    val type: String?
) {
    @Serializable
    data class Result(
        @SerialName("content")
        val content: String?,
        @SerialName("createdAt")
        val createdAt: String?,
        @SerialName("title")
        val title: String?,
        @SerialName("updatedAt")
        val updatedAt: String?
    )
}