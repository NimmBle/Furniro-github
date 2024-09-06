package com.example.service.impl

import com.example.model.Order
import com.example.model.OrderAddBindingModel
import com.example.model.OrderDTO
import com.example.model.OrderProductDTO
import com.example.repository.AddressRepository
import com.example.repository.OrderRepository
import com.example.service.OrderProductService
import com.example.service.OrderService
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class OrderServiceImpl(
    private val orderRepository: OrderRepository = OrderRepository(),
    private val addressRepository: AddressRepository = AddressRepository(),
    private val orderProductService: OrderProductService = OrderProductServiceImpl()
) : OrderService {
    override fun getAll(): List<OrderDTO> {
        val result = orderRepository.getAll().map { it.toDto() }
        return result
    }

    override fun addOrder(orderAddBindingModel: OrderAddBindingModel): OrderDTO {

        val address = addressRepository.addAddress(orderAddBindingModel.address)
        val orderDTO = orderAddBindingModel.toDto()
        orderDTO.addressId = address.id.value

        val order = orderRepository.addOrder(orderDTO).toDto()

        orderAddBindingModel.products.map {
            OrderProductDTO(it.productId, orderDTO.id, it.quantity)
        }.forEach {
            orderProductService.addOrderProduct(it)
        }

        return order
    }

    override fun editOrder(oldName: String, orderAddBindingModel: OrderAddBindingModel) {

        val address = addressRepository.addAddress(orderAddBindingModel.address)

        val orderDTO = orderAddBindingModel.toDto()
        orderDTO.addressId = address.id.value

        orderRepository.editOrder(oldName, orderDTO)
    }

    override fun deleteOrder(id: UUID) {
        orderRepository.deleteOrder(id)
    }

    private fun Order.toDto() = transaction {
        OrderDTO(
            id = UUID.fromString(id),
            firstName = firstName,
            lastName = lastName,
            companyName = companyName,
            phoneNumber = phoneNumber,
            email = email,
            additionalInfo = additionalInfo,
            addressId = address.id.value,
            products = orderProductService.getByOrderId(UUID.fromString(id))
        )
    }

    private fun OrderAddBindingModel.toDto() =
        OrderDTO(
            id = UUID.randomUUID(),
            firstName = firstName,
            lastName = lastName,
            companyName = companyName,
            phoneNumber = phoneNumber,
            email = email,
            additionalInfo = additionalInfo,
            products = emptyList()
        )
}