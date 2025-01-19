package com.example.graphqlktor.schema

import com.example.graphqlktor.services.ProductService
import org.jetbrains.exposed.sql.transactions.transaction

class ProductMutation(private val service: ProductService) {
    fun addProduct(name: String, description: String?, price: Double, stock: Int) = transaction {
        service.addProduct(name, description, price, stock).toDTO()
    }

    fun updateProduct(id: Int, name: String?, description: String?, price: Double?, stock: Int?) = transaction {
        val product = service.getProductById(id) ?: throw IllegalArgumentException("Product not found")
        name?.let { product.name = it }
        description?.let { product.description = it }
        price?.let { product.price = it.toBigDecimal() }
        stock?.let { product.stock = it }
        product.updatedDateTime = java.time.LocalDateTime.now()
        product.toDTO()
    }

    fun deleteProduct(id: Int) = transaction {
        val product = service.getProductById(id) ?: throw IllegalArgumentException("Product not found")
        product.delete()
        true
    }
}
