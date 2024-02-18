package com.plantry.data.dto.response.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfileMissionListDto(
    @SerialName("result")
    val result: List<Result?>?
) {
    @Serializable
    data class Result(
        @SerialName("content")
        val content: String?,
        @SerialName("isAchieved")
        val isAchieved: Boolean?,
        @SerialName("missionId")
        val missionId: Int?
    )
}