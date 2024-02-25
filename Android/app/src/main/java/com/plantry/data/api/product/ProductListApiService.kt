package com.plantry.data.api.product

import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.product.ResponseProductListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductListApiService {
    @GET("/api/v1/pantry/product")
    suspend fun getProductList(
        @Query("pantry") pantry :Int,
        @Query("filter") filter : String,
    ): BaseResponseNullable<ResponseProductListDto>
}