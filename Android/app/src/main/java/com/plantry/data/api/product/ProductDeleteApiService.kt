package com.plantry.data.api.product

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.product.ResponseProductDeleteDto
import retrofit2.http.DELETE
import retrofit2.http.Query

interface ProductDeleteApiService {
    @DELETE("/api/v1/pantry/product")
    suspend fun deleteProduct(
        @Query("product") product : Int,
        @Query("type") type : Int,
        @Query("count") count : Double,
    ): BaseResponseNullable<ResponseProductDeleteDto>
}