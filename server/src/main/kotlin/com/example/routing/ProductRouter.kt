package com.example.routing

import com.example.service.AzureBlobService
import com.example.service.product.ProductService
import com.example.service.impl.CategoryServiceImpl
import com.example.service.impl.product.ProductServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.products(azureBlobService: AzureBlobService) {

    val productService: ProductService = ProductServiceImpl(
        azureBlobService = azureBlobService,
        categoryService = CategoryServiceImpl(azureBlobService = azureBlobService)
    )

    route("/products") {
        get {
            try {
                val products = productService.getAll()
                call.respond(HttpStatusCode.OK, products)
                return@get
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
        }

        post("/add") {
            val multipart = call.receiveMultipart()
            try {
                call.respond(HttpStatusCode.OK, productService.addProduct(multipart))
                return@post
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
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
                call.respond(HttpStatusCode.OK, productService.editProduct(oldName, multipart))
                return@patch
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
                return@patch
            }
        }

        delete("/{name}") {
            val name: String? = call.parameters["name"]
            if (name != null) {
                call.respond(HttpStatusCode.OK, productService.deleteProduct(name))
                return@delete
            } else {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }
        }
    }
}