package com.example.repository

import com.example.model.Product
import com.example.model.Products
import com.example.model.toProduct
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class ProductRepository {

    fun getAll(): List<Product> = transaction {
        Products.selectAll().toList().map { it.toProduct() }
    }

    fun getByProductName(name: String): Product? = transaction {
        Products.selectAll().where { Products.name eq name }.map { it.toProduct() }.firstOrNull()
    }

    fun addProduct(product: Product) = transaction {
        Products.insert {
            it[name] = product.name
            it[price] = product.price
            it[creationDate] = product.creationDate
            it[lastUpdatedDate] = product.lastUpdatedDate
        }
    }

    fun editProduct(oldName: String, product: Product) = transaction {
        Products.update(where = { Products.name eq oldName }) {
            it[name] = product.name
            it[price] = product.price
            it[lastUpdatedDate] = product.lastUpdatedDate
        }
    }

    fun deleteProduct(name: String) = transaction {
        Products.deleteWhere { Products.name eq name }
    }
}