package com.kotlin.refactoring.helper

import com.kotlin.refactoring.model.Product
import com.kotlin.refactoring.model.ProductOption
import com.kotlin.refactoring.model.Role
import com.kotlin.refactoring.model.User
import java.util.*

// Data is from V2_data.sql file

object TestDataFactory {
    val samsungProductId: UUID = UUID.fromString("8f2e9176-35ee-4f0a-ae55-83023d2db1a3")
    val appleProductId: UUID = UUID.fromString("de1287c0-4b15-4a7b-9d8a-dd21b3cafec3")

    fun samsungProduct(): Product =
            Product(samsungProductId, "Samsung Galaxy S7", "Newest mobile product from Samsung.", 1024.99, 16.99)

    val samsungProductAsMap = mutableMapOf(
            "Id" to samsungProductId,
            "Name" to "Samsung Galaxy S7",
            "Description" to "Newest mobile product from Samsung.",
            "Price" to 1024.99,
            "DeliveryPrice" to 16.99
    )

    val samsungProductOptionWhiteId: UUID = UUID.fromString("0643CCF0-AB00-4862-B3C5-40E2731ABCC9")
    val samsungProductOptionBlackId: UUID = UUID.fromString("A21D5777-A655-4020-B431-624BB331E9A2")

    fun samsungProductOptionWhite(): ProductOption =
            ProductOption(id = samsungProductOptionWhiteId, product = samsungProduct(), name = "White", description = "White Samsung Galaxy S7")

    fun samsungProductOptionBlack(): ProductOption =
            ProductOption(id = samsungProductOptionBlackId, product = samsungProduct(), name = "Black", description = "Black Samsung Galaxy S7")

    val samsungProductOptionWhiteAsMap = mutableMapOf(
            "Id" to samsungProductOptionWhiteId,
            "Name" to "White",
            "Description" to "White Samsung Galaxy S7"
    )

    val samsungProductOptionBlackAsMap = mutableMapOf(
            "Id" to samsungProductOptionBlackId,
            "Name" to "Black",
            "Description" to "Black Samsung Galaxy S7"
    )

    fun appleProduct(): Product =
            Product(appleProductId, "Apple iPhone 6S", "Newest mobile product from Apple.", 1299.99, deliveryPrice = 15.99)

    val appleProductAsMap = mutableMapOf(
            "Id" to appleProductId,
            "Name" to "Apple iPhone 6S",
            "Description" to "Newest mobile product from Apple.",
            "Price" to 1299.99,
            "DeliveryPrice" to 15.99
    )

    fun adminRole(): Role =
            Role(id = 4, name = "ADMIN", description = "Admin role")

    fun adminUser(): User =
            User(id = 1, username = "admin", password = "$2a$04\$Ye7/lJoJin6.m9sOJZ9ujeTgHEVM4VXgI2Ingpsnf9gXyXEXf/IlW", roles = setOf(adminRole()))
}
