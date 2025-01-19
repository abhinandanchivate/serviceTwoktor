package com.example.graphqlktor.config

import org.jetbrains.exposed.sql.Database

fun connectToDatabase() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/postgres",
        driver = "org.postgresql.Driver",
        user = "root",
        password = "root"
    )
}
