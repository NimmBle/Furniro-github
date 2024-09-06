package com.example.service

import com.example.model.AddressAddBindingModel
import com.example.model.AddressDTO

interface AddressService {

    fun addAddress(addressAddBindingModel: AddressAddBindingModel): AddressDTO
}