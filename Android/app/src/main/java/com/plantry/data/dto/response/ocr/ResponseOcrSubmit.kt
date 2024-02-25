package com.plantry.data.dto.response.ocr

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseOcrSubmit(
    @SerialName("data")
    val data: List<Product?>?
) {
    @Serializable
    data class Product(
        @SerialName("food_name")
        val food_name: String?,
        @SerialName("quantity")
        val quantity: Int?
    )
}