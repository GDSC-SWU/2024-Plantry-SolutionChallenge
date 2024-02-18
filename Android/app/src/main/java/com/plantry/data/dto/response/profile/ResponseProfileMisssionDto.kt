package com.plantry.data.dto.response.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfileMisssionDto(
    @SerialName("isAchieved")
    val isAchieved: Boolean?,
    @SerialName("missionId")
    val missionId: Int?,
)