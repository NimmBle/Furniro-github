package com.example.routing

import com.example.service.AzureBlobService
import com.example.service.ProductService
import com.example.service.impl.ProductServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.products(azureBlobService: AzureBlobService) {

    val productService: ProductService = ProductServiceImpl(azureBlobService = azureBlobService)

    route("/products") {
        get {
            try {
                val products = productService.getAll()
                call.respond(HttpStatusCode.OK, products)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        post("/add") {
            val multipart = call.receiveMultipart()
            try {
                call.respond(productService.addProduct(multipart))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        patch("/{name}") {
            val multipart = call.receiveMultipart()
            val oldName: String? = call.parameters["name"]

            if (oldName == null) {
                call.respond(HttpStatusCode.NotFound)
                return@patch
            }

            try {
                call.respond(productService.editProduct(oldName, multipart))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        delete("/{name}") {
            val name: String? = call.parameters["name"]
            if (name != null) {
                call.respond(productService.deleteProduct(name))
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}