package com.plantry.data.api.profile

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.request.product.RequestProductAddSingle
import com.plantry.data.dto.response.product.ResponseProductAddDto
import com.plantry.data.dto.response.product.ResponseProductDeleteDto
import com.plantry.data.dto.response.profile.ResponseProfileGetTermDto
import com.plantry.data.dto.response.profile.ResponseProfileInfoDto
import com.plantry.data.dto.response.profile.ResponseProfileMissionListDto
import com.plantry.data.dto.response.profile.ResponseProfileMisssionDto
import com.plantry.data.dto.response.profile.ResponseProfileNameChangeDto
import com.plantry.data.dto.response.profile.ResponseProfileTrakerDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileMisssionSuccessApiService {
    @PATCH("/api/v1/mypage/mission")
    suspend fun patchMissionSuccess(
        @Query ("id") id :String
    ): BaseResponseNullable<ResponseProfileMisssionDto>
}