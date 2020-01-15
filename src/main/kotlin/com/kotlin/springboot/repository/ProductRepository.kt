package com.kotlin.refactoring.repository

import com.kotlin.refactoring.model.Product
import java.util.*
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, UUID> {
//    @Query("SELECT * FROM products where lower(name) like '?1'",
//            nativeQuery = true)
    fun findByNameContainingIgnoreCase(name: String): List<Product>
}
