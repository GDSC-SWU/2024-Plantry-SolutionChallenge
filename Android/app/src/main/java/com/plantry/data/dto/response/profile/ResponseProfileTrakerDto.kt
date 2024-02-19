package com.plantry.data.dto.response.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfileTrackerDto(
    @SerialName("Ingestion")
    val Ingestion: Double?,
    @SerialName("Disposal")
    val Disposal: Double?,
    @SerialName("Sharing")
    val Share: Double?,
    @SerialName("Mistake")
    val Mistake: Double?,
)
