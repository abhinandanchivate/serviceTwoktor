package com.example.graphqlktor.utils

//import graphql.schema.Coercing
//import graphql.schema.CoercingParseLiteralException
//import graphql.schema.CoercingParseValueException
//import graphql.schema.CoercingSerializeException
//import java.time.LocalDate
//import java.time.format.DateTimeFormatter
//
//class LocalDateCoercing : Coercing<LocalDate, String> {
//    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE
//
//    override fun serialize(dataFetcherResult: Any): String {
//        return when (dataFetcherResult) {
//            is LocalDate -> dataFetcherResult.format(formatter)
//            else -> throw CoercingSerializeException("Expected a LocalDate, got $dataFetcherResult")
//        }
//    }
//
//    override fun parseValue(input: Any): LocalDate {
//        return try {
//            LocalDate.parse(input.toString(), formatter)
//        } catch (e: Exception) {
//            throw CoercingParseValueException("Invalid LocalDate value: $input")
//        }
//    }
//
//    override fun parseLiteral(input: Any): LocalDate {
//        val value = (input as? graphql.language.StringValue)?.value
//            ?: throw CoercingParseLiteralException("Expected a StringValue for LocalDate")
//        return try {
//            LocalDate.parse(value, formatter)
//        } catch (e: Exception) {
//            throw CoercingParseLiteralException("Invalid LocalDate literal: $value")
//        }
//    }
//}
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateCoercing : Coercing<LocalDate, String> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(dataFetcherResult: Any): String {
        return when (dataFetcherResult) {
            is LocalDate -> dataFetcherResult.format(formatter)
            else -> throw CoercingSerializeException("Expected a LocalDate, but received: $dataFetcherResult")
        }
    }

    override fun parseValue(input: Any): LocalDate {
        return try {
            LocalDate.parse(input.toString(), formatter)
        } catch (e: Exception) {
            throw CoercingParseValueException("Invalid LocalDate value: $input")
        }
    }

    override fun parseLiteral(input: Any): LocalDate {
        val value = (input as? graphql.language.StringValue)?.value
            ?: throw CoercingParseLiteralException("Expected a StringValue for LocalDate")
        return try {
            LocalDate.parse(value, formatter)
        } catch (e: Exception) {
            throw CoercingParseLiteralException("Invalid LocalDate literal: $value")
        }
    }
}
