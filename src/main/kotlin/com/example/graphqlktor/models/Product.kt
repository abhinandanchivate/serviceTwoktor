package com.example.graphqlktor.models

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.*

object Products : IntIdTable() {
    val name = varchar("name", 255)
    val description = varchar("description", 500).nullable()
    val price = decimal("price", 10, 2)
    val stock = integer("stock")
    val createdDate = date("created_date")
    val updatedDateTime = datetime("updated_date_time")
}

class Product(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Product>(Products)

    var name by Products.name
    var description by Products.description
    var price by Products.price
    var stock by Products.stock
    var createdDate by Products.createdDate
    var updatedDateTime by Products.updatedDateTime
}