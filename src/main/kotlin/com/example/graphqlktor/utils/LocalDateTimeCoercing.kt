package com.example.graphqlktor.utils

import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeCoercing : Coercing<LocalDateTime, String> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun serialize(dataFetcherResult: Any): String {
        return when (dataFetcherResult) {
            is LocalDateTime -> dataFetcherResult.format(formatter)
            else -> throw CoercingSerializeException("Expected a LocalDateTime, got $dataFetcherResult")
        }
    }

    override fun parseValue(input: Any): LocalDateTime {
        return try {
            LocalDateTime.parse(input.toString(), formatter)
        } catch (e: Exception) {
            throw CoercingParseValueException("Invalid LocalDateTime value: $input")
        }
    }

    override fun parseLiteral(input: Any): LocalDateTime {
        val value = (input as? graphql.language.StringValue)?.value
            ?: throw CoercingParseLiteralException("Expected a StringValue for LocalDateTime")
        return try {
            LocalDateTime.parse(value, formatter)
        } catch (e: Exception) {
            throw CoercingParseLiteralException("Invalid LocalDateTime literal: $value")
        }
    }
}
