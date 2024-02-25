package com.plantry.data.api.profile

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.profile.ResponseProfilePermittedSearchAlarmDto
import retrofit2.http.PATCH
import retrofit2.http.Query

interface ProfileAlarmPermittedSetService {
    @PATCH("/api/v1/mypage/notif/permission")
    suspend fun patchProfilePermittedSet(
    ): BaseResponseNullable<ResponseProfilePermittedSearchAlarmDto>
}