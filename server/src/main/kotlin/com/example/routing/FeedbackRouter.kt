package com.example.routing

import com.example.model.FeedbackDTO
import com.example.model.OrderAddBindingModel
import com.example.service.AzureBlobService
import com.example.service.CategoryService
import com.example.service.FeedbackService
import com.example.service.impl.CategoryServiceImpl
import com.example.service.impl.FeedbackServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

fun Route.feedbacks() {

    val feedbackService: FeedbackService = FeedbackServiceImpl()

    route("/feedback") {
        get {
            try {
                val feedbacks = feedbackService.getAll()
                call.respond(HttpStatusCode.OK, Json.encodeToString(feedbacks))
                return@get
            } catch (e: SerializationException) {
                e.printStackTrace()
            }
            catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
        }

        post("/add") {
            try {
                val feedbackDTO = Json.decodeFromString<FeedbackDTO>(call.receiveText())
                feedbackService.addFeedback(feedbackDTO)
                call.respond(HttpStatusCode.OK)
                return@post
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
        }

        delete("/{id}") {
            val id: UUID? = UUID.fromString(call.parameters["id"])
            if (id != null) {
                call.respond(HttpStatusCode.OK, feedbackService.archiveFeedback(id))
                return@delete
            } else {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }
        }
    }
}