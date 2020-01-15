package com.kotlin.refactoring.model.dto

import java.util.stream.Stream
import javax.validation.Validation
import javax.validation.Validator
import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class LoginUserTest {
    private var validator: Validator = Validation.buildDefaultValidatorFactory().validator

    @ParameterizedTest
    @MethodSource("provideInvalidLoginUser")
    fun `validating invalid instance`(reason: String, loginUser: LoginUser) {
        val violations = validator.validate(loginUser)

        Assertions.assertThat(violations).isNotEmpty
        Assertions.assertThat(violations.map { v -> v.message }).containsOnly(reason)
    }

    companion object {

        @JvmStatic
        fun provideInvalidLoginUser(): Stream<Arguments?>? {
            return Stream.of(
                    Arguments.of("Username must not be empty", LoginUser(username = "", password = "password")),
                    Arguments.of("Password must not be empty", LoginUser(username = "username", password = ""))
            )
        }
    }
}
