package com.example.service.product

import java.util.UUID

interface ProductSizeService {

    fun addProductSize(productId: UUID, size: String)

    fun getByProductId(productId: UUID): List<String>

    fun deleteProductSize(productId: UUID, size: String)
}