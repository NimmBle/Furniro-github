package com.example.repository

import com.example.model.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDate
import java.util.UUID

class OrderRepository {

    fun getAll(): List<Order> = transaction {
        Order.all().toList()
    }

    fun addOrder(order: OrderDTO): Order = transaction {
        val entity = Order.new(order.id) {
            this.firstName = order.firstName
            this.lastName = order.lastName
            this.companyName = order.companyName
            this.phoneNumber = order.phoneNumber
            this.email = order.email
            this.additionalInfo = order.additionalInfo
            this.address = Address.findById(order.addressId!!) ?: throw IllegalArgumentException("Address not found")
            this.creationDate = LocalDate.now()
            this.lastUpdatedDate = LocalDate.now()
        }
        return@transaction entity
    }

    fun editOrder(oldName: String, order: OrderDTO) = transaction {
        Orders.update(where = { Orders.firstName eq oldName }) {
            it[firstName] = order.firstName
            it[lastName] = order.lastName
            it[companyName] = order.companyName
            it[phoneNumber] = order.phoneNumber
            it[email] = order.email
            it[additionalInfo] = order.additionalInfo
            it[addressId] = order.addressId!!
        }
    }

    fun deleteOrder(id: UUID) = transaction {
        Orders.deleteWhere { Orders.id eq id }
    }
}