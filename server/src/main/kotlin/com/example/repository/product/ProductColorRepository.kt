package com.example.repository.product

import com.example.model.ProductColor
import com.example.model.ProductColorDTO
import com.example.model.ProductColors
import com.example.model.toProductColor
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.util.*

class ProductColorRepository {

    fun getAll() = transaction {
        ProductColors.selectAll().toList().map { it.toProductColor() }
    }

    fun addProductColor(productColor: ProductColorDTO) = transaction {
        ProductColor.new {
            productId = EntityID(productColor.productId, ProductColors)
            name = productColor.name
            creationDate = LocalDate.now()
            lastUpdatedDate = LocalDate.now()
        }
    }

    fun getByProductId(productId: UUID): List<ProductColor> = transaction {
        ProductColor.find { ProductColors.productId eq productId }.toList()
    }

    fun deleteProductColor(productId: UUID, color: String) = transaction {
        ProductColors.deleteWhere { ProductColors.productId eq productId and (name eq color) }
    }

}