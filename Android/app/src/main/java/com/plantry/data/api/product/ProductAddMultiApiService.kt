package com.plantry.data.api.product

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.request.product.RequestProductAddMulti
import com.plantry.data.dto.response.product.ResponseProductAddDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ProductAddMultiApiService {
    @POST("/api/v1/pantry/product")
    suspend fun postProductAddMulti(
        @Body requestProductAddDto: RequestProductAddMulti
    ): BaseResponseNullable<ResponseProductAddDto>
}