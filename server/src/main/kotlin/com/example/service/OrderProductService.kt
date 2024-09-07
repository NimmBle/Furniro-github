package com.example.service

import com.example.model.OrderProductDTO
import java.util.*

interface OrderProductService {

    fun getByOrderId(orderId: UUID): List<OrderProductDTO>

    fun addOrderProduct(orderProductDTO: OrderProductDTO)

    fun deleteOrderProduct(orderId: UUID, productId: UInt)

}