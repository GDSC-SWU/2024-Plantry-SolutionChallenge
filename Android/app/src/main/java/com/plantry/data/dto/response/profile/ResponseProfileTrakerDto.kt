package com.plantry.data.dto.response.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfileTrakerDto(
    @SerialName("result")
    val result: Result?
) {
    @Serializable
    data class Result(
        @SerialName("Disposal")
        val Disposal: Int?,
        @SerialName("Ingestion")
        val Ingestion: Int?,
        @SerialName("Mistake")
        val Mistake: Int?,
        @SerialName("Share")
        val Share: Int?,
    )
}