package com.example.service.impl

import com.example.model.Product
import com.example.model.ProductDTO
import com.example.repository.ProductPhotoRepository
import com.example.repository.ProductRepository
import com.example.repository.ProductSizeRepository
import com.example.service.ProductPhotoService
import com.example.service.ProductService
import com.example.service.ProductSizeService
import java.util.UUID

class ProductServiceImpl(
    private val productRepository: ProductRepository = ProductRepository(),
    private val productPhotoService: ProductPhotoService = ProductPhotoServiceImpl(),
    private val productSizeService: ProductSizeService = ProductSizeServiceImpl()
) : ProductService {
    override fun getAll(): List<ProductDTO> = productRepository
        .getAll()
        .map { it.toDTO() }
        .onEach { product ->

            if (product.id == null)
                throw IllegalStateException("Product ID cannot be null")

            product.photos.addAll(productPhotoService.getByProductId(product.id))
            product.sizes.addAll(productSizeService.getByProductId(product.id))
        }

    override fun addProduct(product: ProductDTO) {
        if (productRepository.getByProductName(product.name) != null) {
            throw IllegalArgumentException("Product with name ${product.name} already exists")
        }

        val newProduct = Product.new {
            name = product.name
            shortDescription = product.shortDescription
            fullDescription = product.fullDescription
            price = product.price
            discount = product.discount
            stock = product.stock
            markAsNew = product.markAsNew
            coverPhotoUrl = product.coverPhotoUrl
        }

        productRepository.addProduct(newProduct)
    }

    override fun editProduct(oldName: String, product: ProductDTO) {
        val existingProduct = productRepository.getByProductName(oldName)
            ?: throw IllegalArgumentException("Product with name $oldName does not exist")

        existingProduct.apply {
            name = product.name
            shortDescription = product.shortDescription
            fullDescription = product.fullDescription
            price = product.price
            discount = product.discount
            stock = product.stock
            markAsNew = product.markAsNew
            coverPhotoUrl = product.coverPhotoUrl
        }

        val photos = productPhotoService.getByProductId(existingProduct.id.value)

        product.photos.forEach {
            if (it !in photos) {
                TODO()
//                productPhotoService.addProductPhoto(existingProduct.id, pro)
            }
        }

        val sizes = productSizeService.getByProductId(existingProduct.id.value)

        productRepository.editProduct(oldName, existingProduct)
    }

    override fun deleteProduct(name: String) {
        productRepository.deleteProduct(name)
    }

    private fun Product.toDTO() = ProductDTO(
        id = id.value,
        name = name,
        shortDescription = shortDescription,
        fullDescription = fullDescription,
        price = price,
        discount = discount,
        stock = stock,
        markAsNew = markAsNew,
        coverPhotoUrl = coverPhotoUrl,
        photos = mutableListOf(),
        sizes = mutableListOf(),
        colors = mutableListOf()
    )
}