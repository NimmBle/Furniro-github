package com.example.service

import com.example.model.OrderAddBindingModel
import com.example.model.OrderDTO
import java.util.UUID

interface OrderService {

    fun getAll(): List<OrderDTO>

    fun addOrder(orderAddBindingModel: OrderAddBindingModel): OrderDTO

    fun editOrder(oldName: String, orderAddBindingModel: OrderAddBindingModel)

    fun deleteOrder(id: UUID)
}