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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Route.products(azureBlobService: AzureBlobService) {

    val productService: ProductService = ProductServiceImpl(
        azureBlobService = azureBlobService,
        categoryService = CategoryServiceImpl(azureBlobService = azureBlobService)
    )

    route("/products") {
        get {
            try {
                val products = productService.getAll()
                call.respond(HttpStatusCode.OK, Json.encodeToString(products))
                return@get
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
        }

        get("/{page}/{size}") {
            val page: Int = call.parameters["page"]?.toInt() ?: 0
            val size: Long = call.parameters["size"]?.toLong() ?: 10

            try {
                val products = productService.getPagination(page, size)
                call.respond(HttpStatusCode.OK, Json.encodeToString(products))
                return@get
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
        }

        post("/add") {
            val multipart = call.receiveMultipart()
            try {
                val product = productService.addProduct(multipart)
                call.respond(HttpStatusCode.OK, Json.encodeToString(product))
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