package com.example.service.impl.product

import com.example.repository.product.ProductPhotoRepository
import com.example.service.product.ProductPhotoService
import java.util.*

class ProductPhotoServiceImpl(private val productPhotoRepository: ProductPhotoRepository = ProductPhotoRepository()) :
    ProductPhotoService {

        override fun addProductPhoto(productId: UUID, photoUrl: String) {
            productPhotoRepository.addProductPhoto(productId, photoUrl)
        }

        override fun deleteProductPhoto(productId: UUID, photoUrl: String) {
            productPhotoRepository.deleteProductPhoto(productId, photoUrl)
        }

        override fun getByProductId(productId: UUID): List<String> =
            productPhotoRepository.getByProductId(productId).map { it.imageUrl }
}