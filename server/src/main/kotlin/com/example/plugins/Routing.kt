package com.example.plugins

import com.example.routing.categories
import com.example.service.impl.AzureBlobServiceImpl
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val connectionString = environment.config.property("azure.connection_string").getString()
    val containerName = environment.config.property("azure.container_name").getString()

    val azureBlobService = AzureBlobServiceImpl(connectionString, containerName)

    routing {
        categories(azureBlobService)
        staticResources("/static", "static")
    }
}
