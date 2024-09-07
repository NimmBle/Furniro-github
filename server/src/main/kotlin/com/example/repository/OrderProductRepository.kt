package com.example.repository

import com.example.model.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.util.UUID

class OrderProductRepository {
    
    fun getByOrderId(orderId: UUID): List<OrderProduct> = transaction {
        OrderProduct.find { OrdersProducts.orderId eq orderId }.toList()
    }

    fun addOrderProduct(orderProduct: OrderProductDTO) = transaction {
        OrderProduct.new {
            this.order = Order.findById(orderProduct.orderId ?: throw IllegalArgumentException("Order not found")) ?: throw IllegalArgumentException("Order not found")
            this.product = Product.findById(orderProduct.productId ?: throw IllegalArgumentException("Product not found")) ?: throw IllegalArgumentException("Product not found")
            this.quantity = orderProduct.quantity
            this.creationDate = LocalDate.now()
            this.lastUpdatedDate = LocalDate.now()
        }
    }

    fun deleteOrderProduct(orderId: UUID, productId: UUID) = transaction {
        OrdersProducts.deleteWhere { OrdersProducts.orderId eq orderId and (OrdersProducts.productId eq productId) }
    }
}