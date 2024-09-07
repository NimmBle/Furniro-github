package com.example.model

import com.example.plugins.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

object Addresses : BaseTable("addresses") {
    val city = varchar("city", 256)
    val country = varchar("country", 256)
    val address = varchar("address", 512)
    val postalCode = varchar("postal_code", 4)
}

class Address(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Address>(Addresses)

    var city by Addresses.city
    var country by Addresses.country
    var address by Addresses.address
    var postalCode by Addresses.postalCode
    var creationDate by Addresses.creationDate
    var lastUpdatedDate by Addresses.lastUpdatedDate
}

@Serializable
data class AddressDTO(
    @Serializable(with = UUIDSerializer::class) val id: UUID? = null,
    val city: String,
    val country: String,
    val address: String,
    val postalCode: String
)

@Serializable
data class AddressAddBindingModel(
    val city: String,
    val country: String,
    val address: String,
    val postalCode: String
)