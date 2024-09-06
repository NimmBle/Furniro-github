package com.example.repository

import com.example.model.Categories
import com.example.model.Category
import com.example.model.toCategory
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class CategoryRepository {

    fun getAll(): List<Category> = transaction {
        val categories = Categories.selectAll().toList().map { it.toCategory() }
        return@transaction categories
    }

    fun addCategory(category: Category) = transaction {
        Categories.insert {
            it[name] = category.name
            it[coverPhotoUrl] = category.coverPhotoUrl
            it[creationDate] = category.creationDate
            it[lastUpdatedDate] = category.lastUpdatedDate
        }
    }

    fun editCategory(oldName: String, category: Category) = transaction {
        Categories.update(where = { Categories.name eq oldName }) {
            it[name] = category.name
            it[coverPhotoUrl] = category.coverPhotoUrl
            it[lastUpdatedDate] = category.lastUpdatedDate
        }
    }

    fun deleteCategory(name: String) = transaction {
        Categories.deleteWhere { Categories.name eq name }
    }

    fun getCategoryByName(name: String): Category? = transaction {
        Categories.selectAll().where { Categories.name eq name }.map { it.toCategory() }.firstOrNull()
    }
}