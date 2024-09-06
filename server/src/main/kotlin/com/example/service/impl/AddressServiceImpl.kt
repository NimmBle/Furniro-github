package com.example.service.impl

import com.example.model.Address
import com.example.model.AddressAddBindingModel
import com.example.model.AddressDTO
import com.example.repository.AddressRepository
import com.example.service.AddressService

class AddressServiceImpl(private val addressRepository: AddressRepository = AddressRepository()) : AddressService {
    override fun addAddress(addressAddBindingModel: AddressAddBindingModel): AddressDTO =
        addressRepository.addAddress(addressAddBindingModel).toDTO()

    private fun Address.toDTO() = AddressDTO(
        id = id.value,
        city = city,
        country = country,
        address = address,
        postalCode = postalCode
    )
}