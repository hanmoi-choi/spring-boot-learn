package com.kotlin.refactoring.model.dto

import com.kotlin.refactoring.model.Product
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class ProductParam(
    @get: NotBlank(message = "Name cannot be empty")
    @get: Size(max = 17, message = "Name must be less than 18 chars")
    val name: String = "",

    @get: Size(max = 35, message = "Description must be less than 35 chars")
    val description: String? = null,

    @get: Min(value = 0L, message = "Price cannot be negative value")
    @get: DecimalMax(value = "9999.99", inclusive = true, message = "Price must be less than or equal to 9999.99")
    val price: Double = 0.0,

    @get: Min(value = 0L, message = "Delivery Price cannot be negative value")
    @get: DecimalMax(value = "99.99", inclusive = true, message = "Delivery Price must be less than or equal to 99.99")
    val deliveryPrice: Double = 0.0
) {
        fun asProduct(): Product = Product(
                name = this.name,
                description = this.description,
                price = this.price,
                deliveryPrice = this.deliveryPrice
        )
}
