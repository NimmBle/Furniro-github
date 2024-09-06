package com.example.routing

import com.example.model.OrderAddBindingModel
import com.example.service.OrderService
import com.example.service.impl.OrderServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

fun Route.orders() {

    val orderService: OrderService = OrderServiceImpl()

    route("/orders") {
        get("/") {
            try {
                val orders = orderService.getAll()
                call.respond(Json.encodeToString(orders))
                return@get
            } catch (e: Throwable) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
        }

        post("/add") {
            val orderAddBindingModel = Json.decodeFromString<OrderAddBindingModel>(call.receiveText())
            try {
                orderService.addOrder(orderAddBindingModel)
                call.respond(HttpStatusCode.OK)
                return@post
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
        }

        patch("/{name}") {
            val oldName: String? = call.parameters["name"]
            val orderAddBindingModel = Json.decodeFromString<OrderAddBindingModel>(call.receiveText())

            if (oldName == null) {
                call.respond(HttpStatusCode.NotFound)
                return@patch
            }

            try {
                call.respond(HttpStatusCode.OK, orderService.editOrder(oldName, orderAddBindingModel))
                return@patch
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
                return@patch
            }
        }

        delete("/{id}") {
            val id: UUID = call.parameters["id"]?.let { UUID.fromString(it) } ?: return@delete call.respond(HttpStatusCode.BadRequest)
            call.respond(HttpStatusCode.OK, orderService.deleteOrder(id))
            return@delete
        }
    }
}