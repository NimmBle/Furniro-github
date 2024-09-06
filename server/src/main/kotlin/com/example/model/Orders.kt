package com.example.model

import com.example.plugins.UUIDSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import java.util.*

object Orders : BaseTable("orders") {
    val firstName = varchar("first_name", 256)
    val lastName = varchar("last_name", 256)
    val companyName = varchar("company_name", 256).nullable()
    val phoneNumber = varchar("phone_number", 12)
    val email = varchar("email", 256)
    val additionalInfo = text("additional_info").nullable()
    val addressId = reference("address_id", Addresses.id)
}

class Order(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Order>(Orders)

    var firstName by Orders.firstName
    var lastName by Orders.lastName
    var companyName by Orders.companyName
    var phoneNumber by Orders.phoneNumber
    var email by Orders.email
    var additionalInfo by Orders.additionalInfo
    var address by Address referencedOn Orders.addressId
    var creationDate by Orders.creationDate
    var lastUpdatedDate by Orders.lastUpdatedDate
}

object OrdersProducts : BaseTable("orders_products") {
    val orderId = reference("order_id", Orders.id)
    val productId = reference("product_id", Products.id)
    val quantity = uinteger("quantity")
}

class OrderProduct(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<OrderProduct>(OrdersProducts)

    var order by Order referencedOn OrdersProducts.orderId
    var product by Product referencedOn OrdersProducts.productId
    var quantity by OrdersProducts.quantity
    var creationDate by OrdersProducts.creationDate
    var lastUpdatedDate by OrdersProducts.lastUpdatedDate
}

@Serializable
data class OrderDTO(
    @Serializable(with = UUIDSerializer::class) val id: UUID? = null,
    @Serializable(with = UUIDSerializer::class) var addressId: UUID? = null,
    val firstName: String,
    val lastName: String,
    val companyName: String?,
    val phoneNumber: String,
    val email: String,
    val additionalInfo: String?,
    val products: List<OrderProductDTO>
)

@Serializable
data class OrderAddBindingModel(
    val firstName: String,
    val lastName: String,
    val companyName: String?,
    val phoneNumber: String,
    val email: String,
    val additionalInfo: String?,
    val address: AddressAddBindingModel,
    val products: List<OrderProductAddBindingModel>
)

@Serializable
data class OrderProductAddBindingModel (
    @Serializable(with = UUIDSerializer::class) val productId: UUID? = null,
    val quantity: UInt
)

@Serializable
data class OrderProductDTO (
    @Serializable(with = UUIDSerializer::class) val productId: UUID? = null,
    @Serializable(with = UUIDSerializer::class) val orderId: UUID? = null,
    val quantity: UInt
)

fun ResultRow.toOrder() = Order(
    id = this[Orders.id]
).apply {
    firstName = this@toOrder[Orders.firstName]
    lastName = this@toOrder[Orders.lastName]
    companyName = this@toOrder[Orders.companyName]
    phoneNumber = this@toOrder[Orders.phoneNumber]
    email = this@toOrder[Orders.email]
    additionalInfo = this@toOrder[Orders.additionalInfo]
    address =
        Address.findById(this@toOrder[Orders.addressId].value) ?: throw IllegalArgumentException("Address not found")
    creationDate = this@toOrder[Orders.creationDate]
    lastUpdatedDate = this@toOrder[Orders.lastUpdatedDate]
}