package com.kotlin.refactoring.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class Items<T>(
    @JsonProperty("Items")
    val items: List<T>
)
