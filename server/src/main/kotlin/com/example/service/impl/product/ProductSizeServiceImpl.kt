package com.example.service.impl.product

import com.example.repository.product.ProductSizeRepository
import com.example.service.product.ProductSizeService
import java.util.*

class ProductSizeServiceImpl(private val productSizeRepository: ProductSizeRepository = ProductSizeRepository()) :
    ProductSizeService {

    override fun addProductSize(productId: UUID, size: String) {
        productSizeRepository.addProductSize(productId, size)
    }

    override fun getByProductId(productId: UUID): List<String> =
        productSizeRepository.getByProductId(productId).map { it.size.name }

    override fun deleteProductSize(productId: UUID, size: String) {
        productSizeRepository.deleteProductSize(productId, size)
    }
}