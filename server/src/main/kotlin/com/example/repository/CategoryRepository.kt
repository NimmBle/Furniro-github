package com.example.repository

import com.example.model.Categories
import com.example.model.Category
import com.example.model.CategoryDTO
import com.example.model.toCategory
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

class CategoryRepository {

    fun getAll(): List<Category> = transaction {
        val categories = Categories.selectAll().toList().map { it.toCategory() }
        return@transaction categories
    }

    fun addCategory(category: CategoryDTO) = transaction {
        Categories.insert {
            it[name] = category.name
            it[coverPhotoUrl] = category.coverPhotoUrl
            it[creationDate] = LocalDate.now()
            it[lastUpdatedDate] = LocalDate.now()
        }
    }

    fun editCategory(oldName: String, category: CategoryDTO) = transaction {
        Categories.update(where = { Categories.name eq oldName }) {
            it[name] = category.name
            it[coverPhotoUrl] = category.coverPhotoUrl
            it[lastUpdatedDate] = LocalDate.now()
        }
    }

    fun deleteCategory(name: String) = transaction {
        Categories.deleteWhere { Categories.name eq name }
    }

    fun getCategoryByName(name: String): Category? = transaction {
        Category.find { Categories.name eq name }.firstOrNull()
    }
}