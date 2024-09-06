package com.example.routing

import com.example.service.AzureBlobService
import com.example.service.CategoryService
import com.example.service.impl.CategoryServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.categories(azureBlobService: AzureBlobService) {

    val categoryService: CategoryService = CategoryServiceImpl(azureBlobService = azureBlobService)

    route("/categories") {
        get {
            try {
                val categories = categoryService.getAllCategories()
                call.respond(HttpStatusCode.OK, categories)
                return@get
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
        }

        post("/add") {
            val multipart = call.receiveMultipart()
            try {
                call.respond(HttpStatusCode.OK, categoryService.createCategory(multipart))
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
                call.respond(HttpStatusCode.OK, categoryService.updateCategory(oldName, multipart))
                return@patch
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
                return@patch
            }
        }

        delete("/{name}") {
            val name: String? = call.parameters["name"]
            if (name != null) {
                call.respond(HttpStatusCode.OK, categoryService.deleteCategory(name))
                return@delete
            } else {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }
        }
    }
}