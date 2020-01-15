package com.kotlin.refactoring.service.impl

import com.kotlin.refactoring.helper.TestDataFactory.appleProduct
import com.kotlin.refactoring.helper.TestDataFactory.samsungProduct
import com.kotlin.refactoring.helper.TestDataFactory.samsungProductId
import com.kotlin.refactoring.helper.TestDataFactory.samsungProductOptionBlack
import com.kotlin.refactoring.helper.TestDataFactory.samsungProductOptionWhite
import com.kotlin.refactoring.helper.TestDataFactory.samsungProductOptionWhiteId
import com.kotlin.refactoring.model.Product
import com.kotlin.refactoring.model.ProductOption
import com.kotlin.refactoring.model.dto.ProductOptionParam
import com.kotlin.refactoring.model.dto.ProductParam
import com.kotlin.refactoring.model.error.ProductNotFoundError
import com.kotlin.refactoring.model.error.ProductOptionNotFoundError
import com.kotlin.refactoring.repository.ProductRepository
import java.util.*
import javax.transaction.Transactional
import org.assertj.core.api.Assertions.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Transactional
@Tag("IntegrationTest")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
internal class ProductServiceImplTest @Autowired constructor(
    productRepository: ProductRepository
) {
    private val productService = ProductServiceImpl(productRepository)

    @Test
    fun `findAllProducts should return all products without Product Name`() {
        val expectedProducts = listOf(samsungProduct(), appleProduct())
        val products = productService.findAllProducts(null)

        assertThat(products).isEqualTo(expectedProducts)
    }

    @Test
    fun `findAllProducts should return products having the given Product Name`() {
        val expectedProducts = listOf(samsungProduct())
        val products = productService.findAllProducts("Samsung Galaxy S7")

        assertThat(products).isEqualTo(expectedProducts)
    }

    @Test
    fun `findAllProducts should return products having the given Product Name partially`() {
        val expectedProducts = listOf(samsungProduct())
        val products = productService.findAllProducts("Samsung")

        assertThat(products).isEqualTo(expectedProducts)
    }

    @Test
    fun `findProductById should return a product having the ID`() {
        val product: Product = productService.findProductById(samsungProductId)

        assertThat(product).isEqualTo(samsungProduct())
    }

    @Test
    fun `findProductById should throw Exception when there is no record for the given Product ID`() {
        val randomId = UUID.randomUUID()

        assertThatThrownBy { productService.findProductById(randomId) }
                .isInstanceOf(ProductNotFoundError::class.java)
                .hasMessageContaining("Could not find Product with Id: $randomId")
    }

    @Test
    fun `createProduct should create a product`() {
        val name = "Google Pixel3"
        val productParam = ProductParam(name, "Newest mobile product from Google.", 1024.99, 16.99)
        val product: Product = productService.createProduct(productParam)

        val searchedProduct: Product = productService.findProductById(product.id)

        assertThat(searchedProduct.name).isEqualTo(name)
    }

    @Test
    fun `updateProduct should update the product for the given Product Id`() {
        val name = "Samsung Galaxy 10"
        val productParam = ProductParam(name, "Newest mobile product from Samsung.", 1024.99, 16.99)

        productService.updateProduct(samsungProductId, productParam)
        val searchedProduct: Product = productService.findProductById(samsungProductId)

        assertThat(searchedProduct.name).isEqualTo(name)
    }

    @Test
    fun `updateProduct should throw Exception when there is no record for the given Product ID`() {
        val randomId = UUID.randomUUID()
        val productParam = ProductParam("foobar", "Newest mobile product from Samsung.", 1024.99, 16.99)

        assertThatThrownBy { productService.updateProduct(randomId, productParam) }
                .isInstanceOf(ProductNotFoundError::class.java)
                .hasMessageContaining("Could not find Product with Id: $randomId")
    }

    @Test
    fun `deleteProduct should remove the product for the given Product Id from DB`() {
        productService.deleteProduct(samsungProductId)

        assertThatThrownBy { productService.findProductById(samsungProductId) }
                .isInstanceOf(ProductNotFoundError::class.java)
                .hasMessageContaining("Could not find Product with Id: 8f2e9176-35ee-4f0a-ae55-83023d2db1a3")
    }

    @Test
    fun `deleteProduct should throw Exception when there is no record for the given Product ID`() {
        val randomId = UUID.randomUUID()

        assertThatThrownBy { productService.deleteProduct(randomId) }
                .isInstanceOf(ProductNotFoundError::class.java)
                .hasMessageContaining("Could not find Product with Id: $randomId")
    }

    @Test
    fun `findAllProductOptions should return all options for the given Product ID`() {
        val options: List<ProductOption> = productService.findAllProductOptions(samsungProductId)

        assertThat(options).containsExactlyInAnyOrder(samsungProductOptionBlack(), samsungProductOptionWhite())
    }

    @Test
    fun `findAllProductOptions should throw Exception when there is no record for the given Product ID`() {
        val randomId: UUID = UUID.randomUUID()

        assertThatThrownBy { productService.findAllProductOptions(randomId) }
                .isInstanceOf(ProductNotFoundError::class.java)
                .hasMessageContaining("Could not find Product with Id: $randomId")
    }

    @Test
    fun `findProductOptionsById should return all options for the given Product ID`() {
        val options: ProductOption = productService.findProductOptionsById(samsungProductId, samsungProductOptionWhiteId)

        assertThat(options).isEqualTo(samsungProductOptionWhite())
    }

    @Test
    fun `findProductOptionsById should throw Exception when there is no record for the given Product ID`() {
        val randomId: UUID = UUID.randomUUID()

        assertThatThrownBy { productService.findProductOptionsById(randomId, samsungProductOptionWhiteId) }
                .isInstanceOf(ProductNotFoundError::class.java)
                .hasMessageContaining("Could not find Product with Id: $randomId")
    }

    @Test
    fun `findProductOptionsById should throw Exception when there is no record for the given Product Option ID`() {
        val randomId: UUID = UUID.randomUUID()

        assertThatThrownBy { productService.findProductOptionsById(samsungProductId, randomId) }
                .isInstanceOf(ProductOptionNotFoundError::class.java)
                .hasMessageContaining("Could not find ProductOption with Id: $randomId")
    }

    @Test
    fun `createProductOption should create Product Option for the given Product ID`() {
        val newProductOptionParam = ProductOptionParam(productId = samsungProductId, name = "new option", description = "foobar")
        productService.createProductOption(samsungProductId, newProductOptionParam)

        val options = productService.findAllProductOptions(samsungProductId)

        assertThat(options.find { it.name == "new option" }).isNotNull
    }

    @Test
    fun `createProductOption should throw Exception when there is no record for the given Product ID`() {
        val newProductOptionParam = ProductOptionParam(productId = samsungProductId, name = "new option", description = "foobar")
        val randomId: UUID = UUID.randomUUID()

        assertThatThrownBy { productService.createProductOption(randomId, newProductOptionParam) }
                .isInstanceOf(ProductNotFoundError::class.java)
                .hasMessageContaining("Could not find Product with Id: $randomId")
    }

    @Test
    fun `deleteProductOption should delete Product Option for the given Product ID and ProductOption ID`() {
        productService.deleteProductOption(samsungProductId, samsungProductOptionWhiteId)

        assertThatThrownBy { productService.findProductOptionsById(samsungProductId, samsungProductOptionWhiteId) }
                .isInstanceOf(ProductOptionNotFoundError::class.java)
                .hasMessageContaining("Could not find ProductOption with Id: $samsungProductOptionWhiteId")
    }

    @Test
    fun `deleteProductOption should throw Exception when there is no record for the given Product ID`() {
        val randomId: UUID = UUID.randomUUID()

        assertThatThrownBy { productService.deleteProductOption(randomId, samsungProductOptionWhiteId) }
                .isInstanceOf(ProductNotFoundError::class.java)
                .hasMessageContaining("Could not find Product with Id: $randomId")
    }

    @Test
    fun `deleteProductOption should throw Exception when there is no record for the given Product Option ID`() {
        val randomId: UUID = UUID.randomUUID()

        assertThatThrownBy { productService.deleteProductOption(samsungProductId, randomId) }
                .isInstanceOf(ProductOptionNotFoundError::class.java)
                .hasMessageContaining("Could not find ProductOption with Id: $randomId")
    }

    @Test
    fun `updateProductOption should update Product Option for the given Product ID and ProductOption ID`() {
        val newProductOptionParam = ProductOptionParam(productId = samsungProductId, name = "new option", description = "foobar")

        productService.updateProductOption(samsungProductId, samsungProductOptionWhiteId, newProductOptionParam)
        val updateOption = productService.findProductOptionsById(samsungProductId, samsungProductOptionWhiteId)

        assertThat(updateOption.name).isEqualTo("new option")
        assertThat(updateOption.description).isEqualTo("foobar")
    }

    @Test
    fun `updateProductOption should throw Exception when there is no record for the given Product ID`() {
        val newProductOptionParam = ProductOptionParam(productId = samsungProductId, name = "new option", description = "foobar")
        val randomId: UUID = UUID.randomUUID()

        assertThatThrownBy { productService.updateProductOption(randomId, samsungProductOptionWhiteId, newProductOptionParam) }
                .isInstanceOf(ProductNotFoundError::class.java)
                .hasMessageContaining("Could not find Product with Id: $randomId")
    }

    @Test
    fun `updateProductOption should throw Exception when there is no record for the given Product Option ID`() {
        val newProductOptionParam = ProductOptionParam(productId = samsungProductId, name = "new option", description = "foobar")
        val randomId: UUID = UUID.randomUUID()

        assertThatThrownBy { productService.updateProductOption(samsungProductId, randomId, newProductOptionParam) }
                .isInstanceOf(ProductOptionNotFoundError::class.java)
                .hasMessageContaining("Could not find ProductOption with Id: $randomId")
    }

    /*
    override fun updateProductOption(productId: UUID, productOptionID: UUID, productOptionParam: ProductOptionParam): ProductOption {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

     */
}
