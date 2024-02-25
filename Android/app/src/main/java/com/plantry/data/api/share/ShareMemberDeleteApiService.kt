package com.plantry.data.api.share

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.share.ResponseShareCodeDto
import com.plantry.data.dto.response.share.ResponseShareDeleteMemberDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query


interface ShareMemberDeleteApiService {
    @DELETE("/api/v1/pantry/share/member")
    suspend fun deleteShareMember(
        @Query("pantry") pantry: Int,
        @Query("user") user: Int
    ): BaseResponseNullable<ResponseShareDeleteMemberDto>
}