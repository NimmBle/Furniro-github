package com.example.repository.product

import com.example.model.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDate

class ProductRepository {

    fun getAll(): List<Product> = transaction {
        Products.selectAll().toList().map { it.toProduct() }
    }

    fun getByProductName(name: String): Product? = transaction {
        Product.find { Products.name eq name }.firstOrNull()
    }

    fun addProduct(product: ProductDTO) = transaction {
        Product.new {
            name = product.name
            category = Category.findById(product.categoryId ?: throw IllegalArgumentException("Category not found")) ?: throw IllegalArgumentException("Category not found")
            shortDescription = product.shortDescription
            fullDescription = product.fullDescription
            price = product.price
            discount = product.discount
            stock = product.stock
            markAsNew = product.markAsNew
            coverPhotoUrl = product.coverPhotoUrl
            creationDate = LocalDate.now()
            lastUpdatedDate = LocalDate.now()
        }
    }

    fun editProduct(oldName: String, product: Product) = transaction {
        Products.update(where = { Products.name eq oldName }) {
            it[name] = product.name
            it[categoryId] = product.category.id.value
            it[shortDescription] = product.shortDescription
            it[fullDescription] = product.fullDescription
            it[price] = product.price
            it[discount] = product.discount
            it[stock] = product.stock
            it[markAsNew] = product.markAsNew
            it[coverPhotoUrl] = product.coverPhotoUrl
            it[lastUpdatedDate] = LocalDate.now()
        }
    }

    fun deleteProduct(name: String) = transaction {
        Products.deleteWhere { Products.name eq name }
    }
}