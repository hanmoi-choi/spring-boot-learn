package com.kotlin.refactoring.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "product_options")
data class ProductOption(
    @Id
    @JsonProperty("Id")
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    val product: Product? = null,

    @JsonProperty("Name")
    val name: String = "",

    @JsonProperty("Description")
    val description: String? = null
) : AuditModel()
