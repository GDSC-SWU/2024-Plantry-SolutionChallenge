package com.plantry.data.api.share

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.notification.ResponseNoficationProductListDto
import com.plantry.data.dto.response.share.ResponseShareMemberDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ShareCodeMemberApiService {
    @GET("/api/v1/pantry/share/member")
    suspend fun getShareCodeMember(
        @Query("pantry") pantry: Int
    ): BaseResponseNullable<ResponseShareMemberDto>
}