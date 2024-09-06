package com.example.plugins

import com.example.routing.categories
import com.example.routing.orders
import com.example.routing.products
import com.example.service.impl.AzureBlobServiceImpl
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val connectionString = environment.config.property("azure.connection_string").getString()
    val containerName = environment.config.property("azure.container_name").getString()

    val azureBlobService = AzureBlobServiceImpl(connectionString, containerName)

    intercept(ApplicationCallPipeline.Fallback) {
        if (call.isHandled) return@intercept
        val status = call.response.status() ?: HttpStatusCode.NotFound
        call.respond(status)
    }

    routing {
        categories(azureBlobService)
        products(azureBlobService)
        orders()
        staticResources("/static", "static")
    }
}
