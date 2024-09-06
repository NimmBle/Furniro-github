package com.example.service

import java.util.UUID

interface ProductColorService {

    fun addProductColor(productId: UUID, color: String)

    fun getByProductId(productId: UUID): List<String>

    fun deleteProductColor(productId: UUID, color: String)
}