package com.kotlin.refactoring.model.dto

import com.kotlin.refactoring.model.ProductOption
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class ProductOptionParam(
    @get: NotNull(message = "ProductId cannot be null")
    val productId: UUID? = null,

    @get: NotBlank(message = "Name cannot be empty")
    @get: Size(max = 9, message = "Name must be less than 9 chars")
    val name: String = "",

    @get: Size(max = 23, message = "Description must be less than 23 chars")
    val description: String? = null
) {
        fun asProductOption(): ProductOption = ProductOption(
                name = this.name,
                description = this.description
        )
}
