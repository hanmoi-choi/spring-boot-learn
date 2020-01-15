package com.kotlin.refactoring.model.error

import java.util.*

data class ProductOptionNotFoundError(val id: UUID) : RuntimeException("Could not find ProductOption with Id: $id")
