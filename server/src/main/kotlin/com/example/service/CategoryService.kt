package com.example.service

import com.example.model.CategoryDTO
import io.ktor.http.content.*

interface CategoryService {
    fun getAllCategories(): List<CategoryDTO>
    fun getByName(name: String): CategoryDTO
    suspend fun createCategory(multipart: MultiPartData)
    suspend fun updateCategory(oldName: String, multipart: MultiPartData)
    fun updateCategory(oldName: String, newName: String)
    fun deleteCategory(name: String)
}