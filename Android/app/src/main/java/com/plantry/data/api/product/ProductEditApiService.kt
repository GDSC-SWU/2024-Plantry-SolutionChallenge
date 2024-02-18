package com.plantry.data.api.product

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.request.product.RequestProductAddSingle
import com.plantry.data.dto.response.product.ResponseProductAddDto
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Query

interface ProductEditApiService {
    @PATCH("/api/v1/pantry/product")
    suspend fun patchProductEdit(
        @Query("product") product : Int,
        @Body productData: RequestProductAddSingle
    ): BaseResponseNullable<ResponseProductAddDto>
}