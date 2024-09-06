package com.example.repository

import com.example.model.ProductSize
import com.example.model.ProductSizeEnum
import com.example.model.ProductSizes
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class ProductSizeRepository {

    fun addProductSize(productId: UUID, size: String) = transaction {
        ProductSizes.insert {
            it[this.productId] = productId
            it[this.size] = ProductSizeEnum.valueOf(size)
        }
    }

    fun getByProductId(productId: UUID): List<ProductSize> = transaction {
        ProductSize.find { ProductSizes.productId eq productId }.toList()
    }
}