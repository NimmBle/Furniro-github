package com.example.routing

import com.example.model.ReviewDTO
import com.example.service.ReviewService
import com.example.service.impl.ReviewServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

fun Route.reviews() {

    val reviewService: ReviewService = ReviewServiceImpl()

    route("/reviews") {
        get("/{productId}") {
            try {
                val productId = UUID.fromString(call.parameters["productId"])
                val reviews = reviewService.getByProductId(productId)
                call.respond(HttpStatusCode.OK, Json.encodeToString(reviews))
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
                val reviewDTO = Json.decodeFromString<ReviewDTO>(call.receiveText())
                reviewService.addReview(reviewDTO)
                call.respond(HttpStatusCode.OK)
                return@post
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
        }
    }
}