package com.example.service

import java.util.UUID

interface ProductSizeService {

    fun addProductSize(productId: UUID, size: String)

    fun getByProductId(productId: UUID): List<String>
}