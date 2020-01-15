package com.kotlin.refactoring.service

import com.kotlin.refactoring.model.Product
import com.kotlin.refactoring.model.ProductOption
import com.kotlin.refactoring.model.dto.ProductOptionParam
import com.kotlin.refactoring.model.dto.ProductParam
import java.util.*
import org.springframework.security.access.prepost.PreAuthorize

interface ProductService {
    fun findAllProducts(productName: String?): List<Product>
    fun findProductById(id: UUID): Product

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun createProduct(productParam: ProductParam): Product
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun updateProduct(id: UUID, productParam: ProductParam): Product
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun deleteProduct(id: UUID)

    fun findAllProductOptions(productId: UUID): List<ProductOption>
    fun findProductOptionsById(productId: UUID, productOptionID: UUID): ProductOption

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun createProductOption(productId: UUID, productOptionParam: ProductOptionParam): ProductOption
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun updateProductOption(productId: UUID, productOptionID: UUID, productOptionParam: ProductOptionParam): ProductOption
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun deleteProductOption(productId: UUID, productOptionID: UUID): ProductOption
}
