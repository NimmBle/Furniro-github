package com.example.repository

import com.example.model.ProductColor
import com.example.model.ProductColors
import com.example.model.toProductColor
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class ProductColorRepository {

    fun getAll() = transaction {
        ProductColors.selectAll().toList().map { it.toProductColor() }
    }

    fun addProductColor(productColor: ProductColor) = transaction {
        ProductColors.insert {
            it[productId] = productColor.productId
            it[name] = productColor.name
        }
    }

    fun getByProductId(productId: UUID): List<ProductColor> = transaction {
        ProductColors.selectAll().where { ProductColors.productId eq productId }.map { it.toProductColor() }
    }

    fun deleteProductColor(productId: UUID, color: String) = transaction {
        ProductColors.deleteWhere { ProductColors.productId eq productId and (name eq color) }
    }

}