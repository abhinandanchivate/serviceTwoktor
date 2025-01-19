package com.example.graphqlktor.repository

import com.example.graphqlktor.models.Product
import com.example.graphqlktor.models.Products
import org.jetbrains.exposed.sql.transactions.transaction

class ProductRepository {
    fun getAllProducts() = transaction {
        Product.all().toList()
    }

    fun getProductById(id: Int) = transaction {
        Product.findById(id)
    }

    fun addProduct(product: Product): Product = transaction {
        product
    }
}