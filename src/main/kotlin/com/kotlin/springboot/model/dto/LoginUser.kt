package com.kotlin.refactoring.model.dto

import javax.validation.constraints.NotEmpty

data class LoginUser(
    @get: NotEmpty(message = "Username must not be empty")
    var username: String = "",

    @get: NotEmpty(message = "Password must not be empty")
    var password: String = ""
)
