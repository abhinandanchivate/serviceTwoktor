package com.example.graphqlktor.utils

import com.expediagroup.graphql.generator.federation.FederatedSchemaGeneratorHooks
import graphql.schema.GraphQLScalarType
import graphql.schema.GraphQLType
import kotlin.reflect.KType

class CustomFederatedSchemaGeneratorHooks(
    private val customScalars: Map<KType, GraphQLScalarType>
) : FederatedSchemaGeneratorHooks(emptyList()) {
    override fun willGenerateGraphQLType(type: KType): GraphQLType? {
        return customScalars[type] ?: super.willGenerateGraphQLType(type)
    }
}
