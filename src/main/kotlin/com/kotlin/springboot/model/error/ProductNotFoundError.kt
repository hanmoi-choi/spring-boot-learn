package com.kotlin.refactoring.model.error

import java.util.*

data class ProductNotFoundError(val id: UUID) : RuntimeException("Could not find Product with Id: $id")
