package com.kotlin.refactoring.model.dto

import com.kotlin.refactoring.model.ProductOption
import java.util.*
import java.util.stream.Stream
import javax.validation.Validation
import javax.validation.Validator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class ProductOptionParamTest {
    private var validator: Validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun `could create Product instance having the same property`() {
        val productId = UUID.randomUUID()
        val param = ProductOptionParam(productId = productId, name = "foobar", description = "desc")
        val expectedProduct = ProductOption(name = "foobar", description = "desc")

        assertThat(param.asProductOption()).isEqualToComparingOnlyGivenFields(expectedProduct, "name", "description")
    }

    @ParameterizedTest
    @MethodSource("provideInvalidProductOptionParams")
    fun `validating invalid instance`(reason: String, productOptionParam: ProductOptionParam) {
        val violations = validator.validate(productOptionParam)

        assertThat(violations).isNotEmpty
        assertThat(violations.map { v -> v.message }).containsOnly(reason)
    }

    companion object {

        @JvmStatic
        fun provideInvalidProductOptionParams(): Stream<Arguments?>? {
            val moreThan9Chars = "aaaaaaaaaa"
            val moreThan23Chars = "aaaaaaaaaaaaaaaaaaaaaaaa"
            return Stream.of(
                    Arguments.of("ProductId cannot be null", ProductOptionParam(name = "a")),
                    Arguments.of("Name cannot be empty", ProductOptionParam(productId = UUID.randomUUID(), name = "")),
                    Arguments.of("Name must be less than 9 chars", ProductOptionParam(productId = UUID.randomUUID(), name = moreThan9Chars)),
                    Arguments.of("Description must be less than 23 chars", ProductOptionParam(productId = UUID.randomUUID(), name = "foobar", description = moreThan23Chars))
            )
        }
    }
}
