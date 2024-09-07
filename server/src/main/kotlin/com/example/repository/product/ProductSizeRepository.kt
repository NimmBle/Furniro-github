package com.example.repository.product

import com.example.model.ProductSize
import com.example.model.ProductSizeEnum
import com.example.model.ProductSizes
import com.example.model.toProductSize
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.util.*

class ProductSizeRepository {

    fun getAll() = transaction {
        ProductSizes.selectAll().toList().map { it.toProductSize() }
    }

    fun addProductSize(productId: UUID, size: String) = transaction {
        ProductSizes.insert {
            it[this.productId] = productId
            it[this.size] = ProductSizeEnum.valueOf(size)
            it[this.creationDate] = LocalDate.now()
            it[this.lastUpdatedDate] = LocalDate.now()
        }
    }

    fun getByProductId(productId: UUID): List<ProductSize> = transaction {
        ProductSize.find { ProductSizes.productId eq productId }.toList()
    }

    fun deleteProductSize(productId: UUID, size: String) = transaction {
        ProductSizes.deleteWhere {
            (ProductSizes.productId eq productId) and (ProductSizes.size eq ProductSizeEnum.valueOf(size))
        }
    }
}