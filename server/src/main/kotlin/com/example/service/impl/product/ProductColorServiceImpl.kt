package com.example.service.impl.product

import com.example.model.ProductColorDTO
import com.example.repository.product.ProductColorRepository
import com.example.service.product.ProductColorService
import java.util.*

class ProductColorServiceImpl(private val productColorRepository: ProductColorRepository = ProductColorRepository()) :
    ProductColorService {
    override fun addProductColor(productId: UUID, color: String) {
        productColorRepository.addProductColor(
            ProductColorDTO(
                productId = productId,
                name = color
            ))
    }

    override fun getByProductId(productId: UUID): List<String> =
        productColorRepository.getByProductId(productId).map { it.name }

    override fun deleteProductColor(productId: UUID, color: String) {
        productColorRepository.deleteProductColor(productId, color)
    }
}