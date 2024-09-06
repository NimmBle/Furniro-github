package com.example.service

import com.example.model.ProductColor
import com.example.model.ProductColors
import com.example.repository.ProductColorRepository
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class ProductColorServiceImpl(private val productColorRepository: ProductColorRepository = ProductColorRepository()) :
    ProductColorService {
    override fun addProductColor(productId: UUID, color: String) {
        productColorRepository.addProductColor(
            ProductColor.new {
                this.productId = EntityID(productId, ProductColors)
                this.name = color
            })
    }

    override fun getByProductId(productId: UUID): List<String> =
        productColorRepository.getByProductId(productId).map { it.name }

    override fun deleteProductColor(productId: UUID, color: String) {
        productColorRepository.deleteProductColor(productId, color)
    }
}