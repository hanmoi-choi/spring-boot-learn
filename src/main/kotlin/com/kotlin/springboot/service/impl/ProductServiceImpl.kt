package com.kotlin.refactoring.service.impl

import com.kotlin.refactoring.model.Product
import com.kotlin.refactoring.model.ProductOption
import com.kotlin.refactoring.model.dto.ProductOptionParam
import com.kotlin.refactoring.model.dto.ProductParam
import com.kotlin.refactoring.model.error.ProductNotFoundError
import com.kotlin.refactoring.model.error.ProductOptionNotFoundError
import com.kotlin.refactoring.repository.ProductRepository
import com.kotlin.refactoring.service.ProductService
import java.util.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(
    @Autowired val productRepository: ProductRepository
) : ProductService {

    private fun <T> findByProductIdOrError(productId: UUID, lambda: (product: Product) -> T): T {
        return productRepository
                .findById(productId)
                .map { lambda(it) }
                .orElseThrow { ProductNotFoundError(productId) }
    }

    private fun findProductOptionOrError(product: Product, productOptionID: UUID): ProductOption =
            product.options()
                    .firstOrNull { it.id == productOptionID }
                    ?: throw ProductOptionNotFoundError(productOptionID)

    override fun findAllProducts(productName: String?): List<Product> =
            productName?.let {
                productRepository.findByNameContainingIgnoreCase(it)
            } ?: productRepository.findAll()

    override fun findProductById(id: UUID): Product = findByProductIdOrError(id) { it }

    override fun createProduct(productParam: ProductParam): Product = productRepository.save(productParam.asProduct())

    override fun updateProduct(id: UUID, productParam: ProductParam): Product {
        return findByProductIdOrError(id) { p ->
            productRepository.save(
                    p.copy(
                            name = productParam.name,
                            description = productParam.description,
                            price = productParam.price,
                            deliveryPrice = productParam.deliveryPrice
                    )
            )
        }
    }

    override fun deleteProduct(id: UUID) = findByProductIdOrError(id) { productRepository.delete(it) }

    override fun findAllProductOptions(productId: UUID): List<ProductOption> =
            findByProductIdOrError(productId) { it.options() }

    override fun findProductOptionsById(productId: UUID, productOptionID: UUID): ProductOption =
        findByProductIdOrError(productId) {
            findProductOptionOrError(it, productOptionID)
        }

    override fun createProductOption(productId: UUID, productOptionParam: ProductOptionParam): ProductOption {
        return findByProductIdOrError(productId) { p ->
            val newOption = productOptionParam.asProductOption().copy(product = p)

            p.addOption(newOption)
            productRepository.save(p)

            newOption
        }
    }

    override fun updateProductOption(productId: UUID, productOptionID: UUID, productOptionParam: ProductOptionParam): ProductOption {
        return findByProductIdOrError(productId) { p ->
            val option = findProductOptionOrError(p, productOptionID)
            val newOption = option.copy(name = productOptionParam.name, description = productOptionParam.description)

            p.removeOption(option)
            p.addOption(newOption)
            productRepository.save(p)

            newOption
        }
    }

    override fun deleteProductOption(productId: UUID, productOptionID: UUID): ProductOption =
        findByProductIdOrError(productId) { p ->
            val option = findProductOptionOrError(p, productOptionID)

            p.removeOption(option)
            productRepository.save(p)

            option
        }
}
