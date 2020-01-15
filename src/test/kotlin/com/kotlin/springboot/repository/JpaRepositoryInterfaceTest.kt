package com.kotlin.refactoring.repository

import com.kotlin.refactoring.helper.TestDataFactory.adminUser
import com.kotlin.refactoring.helper.TestDataFactory.samsungProduct
import javax.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Tag("IntegrationTest")
internal class JpaRepositoryInterfaceTest @Autowired constructor(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
) {
    // Data is from V2_data.sql file
    @Test
    fun `could find a user with username`() {
        val result = userRepository.findByUsername("admin")

        assertThat(result).isEqualTo(adminUser())
    }

    @Test
    fun `could search products with name`() {
        val expectedProducts = listOf(samsungProduct())
        val result = productRepository.findByNameContainingIgnoreCase("Samsung Galaxy S7")

        assertThat(result).isEqualTo(expectedProducts)
    }

    @Test
    fun `name should be case insensitive`() {
        val expectedProducts = listOf(samsungProduct())
        val result = productRepository.findByNameContainingIgnoreCase("samsung galaxy s7")

        assertThat(result).isEqualTo(expectedProducts)
    }

    @Test
    fun `name could be partially match`() {
        val expectedProducts = listOf(samsungProduct())
        val result = productRepository.findByNameContainingIgnoreCase("samsung")

        assertThat(result).isEqualTo(expectedProducts)
    }
}
