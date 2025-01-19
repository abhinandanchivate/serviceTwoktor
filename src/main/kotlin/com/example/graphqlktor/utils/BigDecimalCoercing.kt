package com.example.graphqlktor.utils

import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import java.math.BigDecimal

class BigDecimalCoercing : Coercing<BigDecimal, String> {
    override fun serialize(dataFetcherResult: Any): String {
        return when (dataFetcherResult) {
            is BigDecimal -> dataFetcherResult.toPlainString()
            else -> throw CoercingSerializeException("Expected a BigDecimal")
        }
    }

    override fun parseValue(input: Any): BigDecimal {
        return try {
            BigDecimal(input.toString())
        } catch (e: NumberFormatException) {
            throw CoercingParseValueException("Invalid value for BigDecimal: $input")
        }
    }

    override fun parseLiteral(input: Any): BigDecimal {
        val value = (input as? graphql.language.StringValue)?.value
            ?: throw CoercingParseLiteralException("Expected a StringValue for BigDecimal")
        return try {
            BigDecimal(value)
        } catch (e: NumberFormatException) {
            throw CoercingParseLiteralException("Invalid literal for BigDecimal: $value")
        }
    }
}
