package com.kotlin.refactoring.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.persistence.*
import javax.persistence.FetchType.LAZY

@Entity
@Table(name = "products")
data class Product(
    @Id
    @JsonProperty("Id")
    val id: UUID = UUID.randomUUID(),

    @Column
    @JsonProperty("Name")
    val name: String = "",

    @Column
    @JsonProperty("Description")
    val description: String? = null,

    @Column
    @JsonProperty("Price")
    val price: Double = 0.0,

    @Column
    @JsonProperty("DeliveryPrice")
    val deliveryPrice: Double = 0.0
) : AuditModel() {
    @OneToMany(
            cascade = [(CascadeType.ALL)],
            orphanRemoval = true,
            fetch = LAZY,
            mappedBy = "product")
    private val _options = mutableListOf<ProductOption>()

    @Transient
    @JsonIgnore
    fun options() = _options.toList()

    fun addOption(option: ProductOption): Unit = this._options.plusAssign(option)
    fun removeOption(option: ProductOption): Unit = this._options.minusAssign(option)
}
