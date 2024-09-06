package com.example.service

import java.util.UUID

interface ProductPhotoService {

    fun addProductPhoto(productId: UUID, photoUrl: String)

    fun deleteProductPhoto(productId: UUID, photoUrl: String)

    fun getByProductId(productId: UUID): List<String>

}