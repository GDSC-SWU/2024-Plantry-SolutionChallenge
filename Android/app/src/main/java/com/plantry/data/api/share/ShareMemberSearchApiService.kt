package com.plantry.data.api.share

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.share.ResponseShareCodeDto
import com.plantry.data.dto.response.share.ResponseShareMemberDto
import retrofit2.http.GET
import retrofit2.http.Query


interface ShareMemberSearchApiService {
    @GET("/api/v1/pantry/share/member/query")
    suspend fun getShareMemberSearch(
        @Query("pantry") pantry: Int,
        @Query("query") query: String
    ): BaseResponseNullable<ResponseShareMemberDto>
}