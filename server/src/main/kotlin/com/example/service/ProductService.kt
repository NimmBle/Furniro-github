package com.example.service

import com.example.model.ProductDTO

interface ProductService {

    fun getAll(): List<ProductDTO>

    fun addProduct(product: ProductDTO)

    fun editProduct(oldName: String, product: ProductDTO)

    fun deleteProduct(name: String)
}