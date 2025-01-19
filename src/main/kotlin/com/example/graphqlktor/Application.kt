package com.example.graphqlktor

import com.example.graphqlktor.config.connectToDatabase
import com.example.graphqlktor.models.Products
import com.example.graphqlktor.repository.ProductRepository
import com.example.graphqlktor.schema.ProductMutation
import com.example.graphqlktor.schema.ProductQuery
import com.example.graphqlktor.services.ProductService
import com.example.graphqlktor.utils.*
import com.expediagroup.graphql.generator.SchemaGeneratorConfig
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.serialization.jackson.*
import io.ktor.server.plugins.contentnegotiation.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils
import com.expediagroup.graphql.generator.TopLevelObject
import com.expediagroup.graphql.generator.federation.FederatedSchemaGeneratorConfig
import com.expediagroup.graphql.generator.federation.FederatedSchemaGeneratorHooks
import com.expediagroup.graphql.generator.federation.toFederatedSchema
import graphql.schema.GraphQLScalarType
import graphql.schema.GraphQLSchema
import java.time.LocalDate
import java.time.LocalDateTime
import io.ktor.server.request.*
import kotlin.reflect.typeOf

fun main() {
    embeddedServer(Netty, port = 8080) {
        // Connect to the database
        connectToDatabase()
        transaction {
            SchemaUtils.create(Products)
        }

        // Generate GraphQL schema
        val schema = graphQLSchemaConfig()

        // Create GraphQL object
        val graphQL = graphql.GraphQL.newGraphQL(schema).build()

        // Install plugins
        install(StatusPages) {
            exception<Throwable> { call, cause ->
                call.respondText(
                    text = "Internal Server Error: ${cause.localizedMessage}",
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        install(ContentNegotiation) {
            jackson()
        }

        // Define routing
        routing {
            post("/graphql") {
                val request = call.receive<Map<String, Any>>()
                val executionResult = graphQL.execute { executionInput ->
                    executionInput
                        .query(request["query"] as String)
                        .operationName(request["operationName"] as String?)
                        .variables(request["variables"] as? Map<String, Any> ?: emptyMap())
                }

                call.respond(executionResult.toSpecification())
            }
        }
    }.start(wait = true)
}
private fun graphQLSchemaConfig(): GraphQLSchema {
    val queries = listOf(
        TopLevelObject(ProductQuery(ProductService(ProductRepository())))
    )
    val mutations = listOf(
        TopLevelObject(ProductMutation(ProductService(ProductRepository())))
    )

    // Define custom scalars
    val localDateScalar = GraphQLScalarType.newScalar()
        .name("LocalDate")
        .description("Custom scalar for java.time.LocalDate")
        .coercing(LocalDateCoercing())
        .build()

    val localDateTimeScalar = GraphQLScalarType.newScalar()
        .name("LocalDateTime")
        .description("Custom scalar for java.time.LocalDateTime")
        .coercing(LocalDateTimeCoercing())
        .build()

    val bigDecimalScalar = GraphQLScalarType.newScalar()
        .name("BigDecimal")
        .description("Custom scalar for BigDecimal")
        .coercing(BigDecimalCoercing())
        .build()

    // Define custom hooks to map scalars
    val hooks = CustomFederatedSchemaGeneratorHooks(
        mapOf(
            typeOf<LocalDate>() to localDateScalar,
            typeOf<LocalDateTime>() to localDateTimeScalar
        )
    )

    // Configure federated schema
    return toFederatedSchema(
        config = FederatedSchemaGeneratorConfig(
            hooks = hooks,
            supportedPackages = listOf("com.example.graphqlktor.models", "com.example.graphqlktor.schema"),
            additionalTypes = setOf(localDateScalar, localDateTimeScalar, bigDecimalScalar)
        ),
        queries = queries,
        mutations = mutations
    )
}

