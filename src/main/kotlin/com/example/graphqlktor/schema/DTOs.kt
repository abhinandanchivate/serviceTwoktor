
package com.example.graphqlktor.schema

import com.example.graphqlktor.models.Product
import com.expediagroup.graphql.generator.annotations.GraphQLType
import com.expediagroup.graphql.generator.federation.directives.FieldSet
import com.expediagroup.graphql.generator.federation.directives.KeyDirective
import java.time.LocalDate
import java.time.LocalDateTime
@KeyDirective(fields = FieldSet("id"))
data class ProductDTO(
    val id: Int,
    val name: String,
    val description: String?,
    val price: Double,
    val stock: Int,
   // @GraphQLType(name = "LocalDate") // Explicitly map to LocalDate scalar
    val createdDate: LocalDate,
   // @GraphQLType(name = "LocalDateTime") // Explicitly map to LocalDateTime scalar
    val updatedDateTime: LocalDateTime
)

fun Product.toDTO() = ProductDTO(
    id.value,
    name,
    description,
    price.toDouble(),
    stock,
    createdDate,
    updatedDateTime
)