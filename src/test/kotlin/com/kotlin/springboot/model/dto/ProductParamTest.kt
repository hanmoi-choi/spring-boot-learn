package com.kotlin.refactoring.model.dto

import com.kotlin.refactoring.model.Product
import java.util.stream.Stream
import javax.validation.Validation
import javax.validation.Validator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class ProductParamTest {
    private var validator: Validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun `could create Product instance having the same property`() {
        val param = ProductParam(name = "foobar", description = "desc", price = 1.0, deliveryPrice = 1.0)
        val expectedProduct = Product(name = "foobar", description = "desc", price = 1.0, deliveryPrice = 1.0)

        assertThat(param.asProduct()).isEqualToComparingOnlyGivenFields(expectedProduct, "name", "description", "price", "deliveryPrice")
    }

    @ParameterizedTest
    @MethodSource("provideInvalidProductParams")
    fun `validating invalid instance`(reason: String, productParam: ProductParam) {
        val violations = validator.validate(productParam)

        assertThat(violations).isNotEmpty
        assertThat(violations.map { v -> v.message }).containsOnly(reason)
    }

    companion object {

        @JvmStatic
        fun provideInvalidProductParams(): Stream<Arguments?>? {
            val moreThan17Chars = "aaaaaaaaaaaaaaaaaa"
            val moreThan35Chars = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            return Stream.of(
                    Arguments.of("Name cannot be empty", ProductParam(name = "")),
                    Arguments.of("Name must be less than 18 chars", ProductParam(name = moreThan17Chars)),
                    Arguments.of("Description must be less than 35 chars", ProductParam(name = "foobar", description = moreThan35Chars)),
                    Arguments.of("Price cannot be negative value", ProductParam(name = "foobar", price = -0.1)),
                    Arguments.of("Price must be less than or equal to 9999.99", ProductParam(name = "foobar", price = 100000.00)),
                    Arguments.of("Delivery Price cannot be negative value", ProductParam(name = "foobar ", deliveryPrice = -0.1)),
                    Arguments.of("Delivery Price must be less than or equal to 99.99", ProductParam(name = "foobar ", deliveryPrice = 100.00))
            )
        }
    }
}
