package com.example.repository

import com.example.model.Address
import com.example.model.AddressAddBindingModel
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

class AddressRepository {

    fun addAddress(addressDTO: AddressAddBindingModel): Address = transaction {
        Address.new {
            this.city = addressDTO.city
            this.country = addressDTO.country
            this.address = addressDTO.address
            this.postalCode = addressDTO.postalCode
            this.creationDate = LocalDate.now()
            this.lastUpdatedDate = LocalDate.now()
        }
    }
}