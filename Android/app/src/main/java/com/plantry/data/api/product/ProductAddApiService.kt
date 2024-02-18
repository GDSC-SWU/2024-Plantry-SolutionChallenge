package com.plantry.data.api.product

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.request.product.RequestProductAddSingle
import com.plantry.data.dto.response.product.ResponseProductAddDto
import com.plantry.data.dto.response.product.ResponseProductDeleteDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductAddApiService {
    @POST("/api/v1/pantry/product/single")
    suspend fun postProductAddSingle(
        @Body requestProductAddDto: RequestProductAddSingle
    ): BaseResponseNullable<ResponseProductAddDto>
}