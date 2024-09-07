package com.example.plugins

import com.example.model.*
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.DriverManager

fun Application.configureDatabases() {

    val url = environment.config.property("postgresql.url").getString()
    val name = environment.config.property("postgresql.name").getString()
    val driver = environment.config.property("postgresql.driver").getString()
    val user = environment.config.property("postgresql.user").getString()
    val password = environment.config.property("postgresql.password").getString()

    Database.connect(
        url = "$url/$name",
        driver = driver,
        user = user,
        password = password
    )

    transaction {
        SchemaUtils.create(Categories)
        SchemaUtils.create(ProductPhotos)
        SchemaUtils.create(ProductSizes)
        SchemaUtils.create(ProductColors)
        SchemaUtils.create(Products)
        SchemaUtils.create(Addresses)
        SchemaUtils.create(Orders)
        SchemaUtils.create(OrdersProducts)
        SchemaUtils.create(Feedbacks)
        SchemaUtils.create(Reviews)
    }
}