package com.kotlin.refactoring.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.kotlin.refactoring.helper.TestDataFactory
import com.kotlin.refactoring.helper.TestDataFactory.appleProductId
import com.kotlin.refactoring.helper.TestDataFactory.samsungProduct
import com.kotlin.refactoring.helper.TestDataFactory.samsungProductId
import com.kotlin.refactoring.helper.TestDataFactory.samsungProductOptionWhite
import com.kotlin.refactoring.helper.TestDataFactory.samsungProductOptionWhiteId
import com.kotlin.refactoring.model.dto.ProductOptionParam
import com.kotlin.refactoring.model.dto.ProductParam
import javax.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Tag("SystemTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
// Test should be executed in order since it is testing against DB
class ProductControllerTest {

    @Autowired
    private lateinit var context: WebApplicationContext

    private lateinit var mvc: MockMvc

    @BeforeAll
    fun setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build()
    }

    @Test
    @Order(0)
    fun `retrieve all products`() {
        val payload = mapOf(
                "Items" to listOf(TestDataFactory.samsungProductAsMap, TestDataFactory.appleProductAsMap)
        )

        val response = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/products")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk).andReturn().response

        assertThat(response.contentAsString).isEqualTo(objectToJson(payload))
    }

    @Test
    @Order(1)
    fun `retrieve products wiht name parameter`() {
        val payload = mapOf(
                "Items" to listOf(TestDataFactory.samsungProductAsMap)
        )

        val response = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/products?name=samsung")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk).andReturn().response

        assertThat(response.contentAsString).isEqualTo(objectToJson(payload))
    }

    @Test
    @Order(2)
    fun `retrieve a product with Id`() {
        val payload = TestDataFactory.samsungProductAsMap
        val productId = TestDataFactory.samsungProductId

        val response = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/products/$productId")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk).andReturn().response

        assertThat(response.contentAsString).isEqualTo(objectToJson(payload))
    }

    @Test
    @Order(3)
    fun `retrieve all product options`() {
        val payload = mapOf(
                "Items" to listOf(TestDataFactory.samsungProductOptionWhiteAsMap, TestDataFactory.samsungProductOptionBlackAsMap)
        )
        val productId = TestDataFactory.samsungProductId

        val response = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/products/$productId/options")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk).andReturn().response

        assertThat(response.contentAsString).isEqualTo(objectToJson(payload))
    }

    @Test
    @Order(4)
    fun `retrieve a product option with Id`() {
        val payload = TestDataFactory.samsungProductAsMap
        val productId = TestDataFactory.samsungProductId

        val response = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/products/$productId")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk).andReturn().response

        assertThat(response.contentAsString).isEqualTo(objectToJson(payload))
    }

    @Test
    @Order(5)
    fun `delete product`() {
        val payload = mapOf(
                "Items" to listOf(TestDataFactory.samsungProductAsMap)
        )
        mvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/v1/products/$appleProductId")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk).andReturn()

        val response = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/products")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk).andReturn().response

        assertThat(response.contentAsString).isEqualTo(objectToJson(payload))
    }

    @Test
    @Order(6)
    fun `delete a product option`() {
        val payload = mapOf(
                "Items" to listOf(TestDataFactory.samsungProductOptionBlackAsMap)
        )
        val productId = TestDataFactory.samsungProductId

        mvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/v1/products/$samsungProductId/options/$samsungProductOptionWhiteId")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk).andReturn()

        val response = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/products/$productId/options")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk).andReturn().response

        assertThat(response.contentAsString).isEqualTo(objectToJson(payload))
    }

    @Test
    @Order(7)
    fun `update product`() {
        val newProductName = "Samsung Galaxy S9"
        val oldProduct = samsungProduct()
        val productParam = ProductParam(name = newProductName, description = oldProduct.description, price = oldProduct.price, deliveryPrice = oldProduct.deliveryPrice)
        val newProduct = TestDataFactory.samsungProductAsMap.also { it.replace("Name", newProductName) }

        mvc.perform(
                MockMvcRequestBuilders
                        .put("/api/v1/products/$samsungProductId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(productParam))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk).andReturn()

        val response = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/products/$samsungProductId")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk).andReturn().response

        assertThat(response.contentAsString).isEqualTo(objectToJson(newProduct))
    }

    @Test
    @Order(8)
    fun `update product option`() {
        val newProductOptionName = "Golden"
        val oldProductOption = samsungProductOptionWhite()
        val productOptionParam = ProductOptionParam(productId = samsungProductId, name = newProductOptionName, description = oldProductOption.description)
        val newProductOption = TestDataFactory.samsungProductOptionWhiteAsMap.also { it.replace("Name", newProductOptionName) }

        mvc.perform(
                MockMvcRequestBuilders
                        .put("/api/v1/products/$samsungProductId/options/$samsungProductOptionWhiteId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(productOptionParam))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk).andReturn()

        val response = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/products/$samsungProductId/options/$samsungProductOptionWhiteId")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk).andReturn().response

        assertThat(response.contentAsString).isEqualTo(objectToJson(newProductOption))
    }

    @Test
    @Order(9)
    fun `create product`() {
        val oldProduct = samsungProduct()
        val productParam = ProductParam(name = "Samsung Galaxy SX", description = oldProduct.description, price = oldProduct.price, deliveryPrice = oldProduct.deliveryPrice)

        val response = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(productParam))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isCreated).andReturn().response

        val newProductId = jsonToMap(response.contentAsString)["Id"] ?: error("")

        mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/products/$newProductId")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk)
    }

    @Test
    @Order(10)
    fun `create product option`() {
        val productOptionParam = ProductOptionParam(productId = samsungProductId, name = "Golden", description = "")

        val response = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/products/$samsungProductId/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(productOptionParam))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk).andReturn().response

        val newProductOptionId = jsonToMap(response.contentAsString)["Id"] ?: error("")

        mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/products/$samsungProductId/options/$newProductOptionId")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk)
    }

    fun objectToJson(obj: Any): String {
      val objectMapper = ObjectMapper()
      return objectMapper.writeValueAsString(obj)
    }

    fun jsonToMap(json: String): Map<String, String> {
        val objectMapper = ObjectMapper()
        return objectMapper.readValue(json)
    }
}
