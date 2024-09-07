package com.example.service.product

import com.example.model.ProductDTO
import io.ktor.http.content.*
import java.util.UUID

interface ProductService {

    fun getAll(): List<ProductDTO>

    fun getById(id: UUID): ProductDTO

    fun getPagination(page: Int, size: Long): List<ProductDTO>

    suspend fun addProduct(multipart: MultiPartData)

    suspend fun editProduct(oldName: String, multipart: MultiPartData)

    fun deleteProduct(name: String)
}