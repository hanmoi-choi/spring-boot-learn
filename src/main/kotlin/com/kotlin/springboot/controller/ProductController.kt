package com.kotlin.refactoring.controller

import com.kotlin.refactoring.model.Product
import com.kotlin.refactoring.model.ProductOption
import com.kotlin.refactoring.model.dto.Items
import com.kotlin.refactoring.model.dto.ProductOptionParam
import com.kotlin.refactoring.model.dto.ProductParam
import com.kotlin.refactoring.service.ProductService
import io.swagger.annotations.Api
import java.util.*
import javax.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
@Api(value = "Product and ProductOption Management API")

class ProductController(
    @Autowired private val productService: ProductService
) {

    @GetMapping("/products")
    fun getAllProducts(@RequestParam("name") productName: String?): Items<Product> {
        return Items(productService.findAllProducts(productName))
    }

    @GetMapping("/products/{productId}")
    fun getProduct(@PathVariable productId: UUID): Product {
        return productService.findProductById(productId)
    }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(@Valid @RequestBody productParam: ProductParam): Product {
        return productService.createProduct(productParam)
    }

    @PutMapping("/products/{productId}")
    fun updateProduct(
        @PathVariable productId: UUID,
        @Valid @RequestBody productParam: ProductParam
    ): Product {
        return productService.updateProduct(productId, productParam)
    }

    @DeleteMapping("/products/{productId}")
    fun deleteProduct(@PathVariable productId: UUID) {
        return productService.deleteProduct(productId)
    }

    @GetMapping("/products/{productId}/options")
    fun getProductOptions(@PathVariable productId: UUID): Items<ProductOption> {
        return Items(productService.findAllProductOptions(productId))
    }

    @GetMapping("/products/{productId}/options/{optionId}")
    fun getProductOption(@PathVariable productId: UUID, @PathVariable optionId: UUID): ProductOption {
        return productService.findProductOptionsById(productId, optionId)
    }

    @PostMapping("/products/{productId}/options")
    fun createProductOption(
        @PathVariable productId: UUID,
        @Valid @RequestBody productOptionParam: ProductOptionParam
    ): ProductOption {
        return productService.createProductOption(productId, productOptionParam)
    }

    @PutMapping("/products/{productId}/options/{optionId}")
    fun updateProductOption(
        @PathVariable productId: UUID,
        @PathVariable optionId: UUID,
        @Valid @RequestBody productOptionParam: ProductOptionParam
    ): ProductOption? {
        return productService.updateProductOption(productId, optionId, productOptionParam)
    }

    @DeleteMapping("/products/{productId}/options/{optionId}")
    fun deleteProductOption(
        @PathVariable productId: UUID,
        @PathVariable optionId: UUID
    ): ProductOption {
        return productService.deleteProductOption(productId, optionId)
    }
}
