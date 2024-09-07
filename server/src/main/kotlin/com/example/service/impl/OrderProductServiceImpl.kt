package com.example.service.impl

import com.example.model.OrderProduct
import com.example.model.OrderProductDTO
import com.example.repository.OrderProductRepository
import com.example.service.OrderProductService
import java.util.UUID

class OrderProductServiceImpl(private val orderProductRepository: OrderProductRepository = OrderProductRepository()) :
    OrderProductService {
    override fun getByOrderId(orderId: UUID): List<OrderProductDTO> =
        orderProductRepository.getByOrderId(orderId).map { it.toDto() }

    override fun addOrderProduct(orderProductDTO: OrderProductDTO) {
        orderProductRepository.addOrderProduct(
            OrderProductDTO(
                orderId = orderProductDTO.orderId,
                productId = orderProductDTO.productId,
                quantity = orderProductDTO.quantity
            )
        )
    }

    override fun deleteOrderProduct(orderId: UUID, productId: UInt) {
        TODO("Not yet implemented")
    }

    private fun OrderProduct.toDto() =
        OrderProductDTO(
            orderId = order.id.value,
            productId = product.id.value,
            quantity = quantity
        )
}

