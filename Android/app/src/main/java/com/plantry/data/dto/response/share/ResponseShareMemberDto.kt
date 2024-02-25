package com.plantry.data.dto.response.share

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseShareMemberDto(
    @SerialName("isUserOwner")
    val isUserOwner: Boolean?,
    @SerialName("list")
    val list: List<User>?
) {
    @Serializable
    data class User(
        @SerialName("userId")
        val userId: Int?,
        @SerialName("isOwner")
        val isOwner: Boolean?,
        @SerialName("nickname")
        val nickname: String?,
        @SerialName("profileImagePath")
        val profileImagePath: String?
    )
}