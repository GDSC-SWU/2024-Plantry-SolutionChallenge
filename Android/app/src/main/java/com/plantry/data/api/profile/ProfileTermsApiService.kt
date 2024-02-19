package com.plantry.data.api.profile

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.request.product.RequestProductAddSingle
import com.plantry.data.dto.response.product.ResponseProductAddDto
import com.plantry.data.dto.response.product.ResponseProductDeleteDto
import com.plantry.data.dto.response.profile.ResponseProfileGetTermDto
import com.plantry.data.dto.response.profile.ResponseProfileInfoDto
import com.plantry.data.dto.response.profile.ResponseProfileNameChangeDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileTermsApiService {
    @GET("/api/v1/mypage/terms")
    suspend fun getTerms(
        @Query ("type") type : String
    ): BaseResponseNullable<ResponseProfileGetTermDto>
}