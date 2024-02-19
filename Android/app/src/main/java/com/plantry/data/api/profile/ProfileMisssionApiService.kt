package com.plantry.data.api.profile

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.profile.ResponseProfileMissionListDto
import retrofit2.http.GET

interface ProfileMisssionApiService {
    @GET("/api/v1/mypage/mission")
    suspend fun getMissionList(
    ): BaseResponseNullable<ResponseProfileMissionListDto>
}