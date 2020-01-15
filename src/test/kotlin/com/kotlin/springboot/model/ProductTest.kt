package com.kotlin.refactoring.model

import com.kotlin.refactoring.helper.TestDataFactory.samsungProduct
import com.kotlin.refactoring.helper.TestDataFactory.samsungProductOptionBlack
import com.kotlin.refactoring.helper.TestDataFactory.samsungProductOptionWhite
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ProductTest {
    @Test
    fun `Could add ProductOption`() {
        // Initially should be empty
        val product = samsungProduct()

        assertThat(product.options().size).isEqualTo(0)

        product.addOption(samsungProductOptionWhite())

        assertThat(product.options().size).isEqualTo(1)
    }

    @Test
    fun `Could delete ProductOption`() {
        // Initially should have one option
        val product = samsungProduct()
        val option = samsungProductOptionWhite()
        product.addOption(option)
        assertThat(product.options().size).isEqualTo(1)

        product.removeOption(option)
        assertThat(product.options().size).isEqualTo(0)
    }

    @Test
    fun `ignore when the given option does not exist`() {
        // Initially should have one option
        val product = samsungProduct()
        val whiteOption = samsungProductOptionWhite()
        val blackOption = samsungProductOptionBlack()

        product.addOption(whiteOption)
        assertThat(product.options().size).isEqualTo(1)

        product.removeOption(blackOption)
        assertThat(product.options().size).isEqualTo(1)
    }
}
