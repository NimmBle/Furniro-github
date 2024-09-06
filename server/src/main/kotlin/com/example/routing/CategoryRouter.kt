package com.example.routing

import com.example.model.CategoryDTO
import com.example.repository.CategoryRepository
import com.example.service.AzureBlobService
import com.example.service.CategoryService
import com.example.service.impl.CategoryServiceImpl
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.ByteArrayOutputStream
import java.io.InputStream

fun Route.categories(azureBlobService: AzureBlobService) {

    val categoryService: CategoryService = CategoryServiceImpl(azureBlobService = azureBlobService)

    route("/categories") {
        get {
            try {
                val categories = categoryService.getAllCategories()
                call.respond(HttpStatusCode.OK, categories)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        post("/add") {
            val multipart = call.receiveMultipart()
            try {
                call.respond(categoryService.createCategory(multipart))
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
                call.respond(categoryService.updateCategory(oldName, multipart))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        delete("/{name}") {
            val name: String? = call.parameters["name"]
            if (name != null) {
                call.respond(categoryService.deleteCategory(name))
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}