package com.example.graphqlktor.schema

import com.example.graphqlktor.services.ProductService
import org.jetbrains.exposed.sql.transactions.transaction

class ProductQuery(private val service: ProductService) {
    fun allProducts() = transaction {
        service.getAllProducts().map { it.toDTO() }
    }

    fun getProductById(id: Int) = transaction {
        service.getProductById(id)?.toDTO()
    }

    fun getProductsByName(name: String) = transaction {
        service.getAllProducts()
            .filter { it.name.contains(name, ignoreCase = true) }
            .map { it.toDTO() }
    }

    fun getProductsByPriceRange(min: Double, max: Double) = transaction {
        service.getAllProducts()
            .filter { it.price.toDouble() in min..max }
            .map { it.toDTO() }
    }

    fun getOutOfStockProducts() = transaction {
        service.getAllProducts()
            .filter { it.stock == 0 }
            .map { it.toDTO() }
    }

    fun getRecentlyUpdatedProducts(limit: Int) = transaction {
        service.getAllProducts()
            .sortedByDescending { it.updatedDateTime }
            .take(limit)
            .map { it.toDTO() }
    }
}