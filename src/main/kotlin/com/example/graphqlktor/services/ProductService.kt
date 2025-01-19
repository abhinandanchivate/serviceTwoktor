package com.example.graphqlktor.services

import com.example.graphqlktor.models.Product
import com.example.graphqlktor.repository.ProductRepository

class ProductService(private val repository: ProductRepository) {
    fun getAllProducts() = repository.getAllProducts()

    fun getProductById(id: Int): Product? = repository.getProductById(id)

    fun addProduct(name: String, description: String?, price: Double, stock: Int): Product {
        return Product.new {
            this.name = name
            this.description = description
            this.price = price.toBigDecimal()
            this.stock = stock
            this.createdDate = java.time.LocalDate.now()
            this.updatedDateTime = java.time.LocalDateTime.now()
        }.also {
            repository.addProduct(it)
        }
    }
}