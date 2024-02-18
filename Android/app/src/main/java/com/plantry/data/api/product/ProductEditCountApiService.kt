package com.plantry.data.api.product

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.product.ResponseProductAddDto
import retrofit2.http.PATCH
import retrofit2.http.Query

interface ProductEditCountApiService {
    @PATCH("/api/v1/pantry/product/count")
    suspend fun patchProductEditCount(
        @Query("product") product :Int,
        @Query("count") count : Double,
    ): BaseResponseNullable<ResponseProductAddDto>
}