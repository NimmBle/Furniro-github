package com.example.service.impl

import com.example.model.Product
import com.example.model.ProductDTO
import com.example.repository.ProductRepository
import com.example.service.*
import io.ktor.http.content.*

class ProductServiceImpl(
    private val productRepository: ProductRepository = ProductRepository(),
    private val productPhotoService: ProductPhotoService = ProductPhotoServiceImpl(),
    private val productSizeService: ProductSizeService = ProductSizeServiceImpl(),
    private val productColorService: ProductColorService = ProductColorServiceImpl(),
    private val azureBlobService: AzureBlobService
) : ProductService {
    override fun getAll(): List<ProductDTO> = productRepository
        .getAll()
        .map { it.toDTO() }
        .onEach { product ->

            if (product.id == null)
                throw IllegalStateException("Product ID cannot be null")

            product.photos.addAll(productPhotoService.getByProductId(product.id))
            product.sizes.addAll(productSizeService.getByProductId(product.id))
            product.colors.addAll(productColorService.getByProductId(product.id))
        }

    override suspend fun addProduct(multipart: MultiPartData) {

        val (first, second) = uploadImages(multipart)
        val (product, imageUrls) = first
        val (colors, sizes) = second

        productRepository.addProduct(product)

        colors.forEach {
            productColorService.addProductColor(product.id.value, it)
        }
        sizes.forEach {
            productSizeService.addProductSize(product.id.value, it)
        }
        imageUrls.forEach {
            productPhotoService.addProductPhoto(product.id.value, it)
        }
    }

    override suspend fun editProduct(oldName: String, multipart: MultiPartData) {
        val existingProduct = productRepository.getByProductName(oldName)
            ?: throw IllegalArgumentException("Product with name $oldName does not exist")

        val (first, second) = uploadImages(multipart)
        val (product, imageUrls) = first
        val (colors, sizes) = second

        colors.forEach {
            val colorsCurr = productColorService.getByProductId(product.id.value)
            if (!colorsCurr.contains(it)) {
                productColorService.addProductColor(product.id.value, it)
            }

            colorsCurr.forEach { curr ->
                if (!colors.contains(curr)) {
                    productColorService.deleteProductColor(product.id.value, curr)
                }
            }
            productColorService.addProductColor(product.id.value, it)
        }
        sizes.forEach {
            val sizesCurr = productSizeService.getByProductId(product.id.value)
            if (!sizesCurr.contains(it)) {
                productSizeService.addProductSize(product.id.value, it)
            }

            sizesCurr.forEach { curr ->
                if (!sizes.contains(curr)) {
                    productSizeService.deleteProductSize(product.id.value, curr)
                }
            }
            productSizeService.addProductSize(product.id.value, it)
        }
        imageUrls.forEach {
            productPhotoService.addProductPhoto(product.id.value, it)
        }

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

    private suspend fun uploadImages(multipart: MultiPartData): Pair<Pair<Product, MutableList<String>>, Pair<List<String>, List<String>>> {

        var name: String? = null
        var shortDescription: String? = null
        var fullDescription: String? = null
        var price: Double? = null
        var discount: Double? = null
        var stock: UInt? = null
        var markAsNew: Boolean? = null

        val colors: MutableList<String> = mutableListOf()
        val sizes: MutableList<String> = mutableListOf()

        val images: MutableList<PartData.FileItem> = mutableListOf()
        var coverImage: PartData.FileItem? = null

        multipart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    when (part.name) {
                        "name" -> name = part.value
                        "shortDescription" -> shortDescription = part.value
                        "fullDescription" -> fullDescription = part.value
                        "price" -> price = part.value.toDouble()
                        "discount" -> discount = part.value.toDouble()
                        "stock" -> stock = part.value.toUInt()
                        "markAsNew" -> markAsNew = part.value.toBoolean()
                        "colors" -> colors.addAll(part.value.split(", "))
                        "sizes" -> sizes.addAll(part.value.split(", "))
                    }
                }
                is PartData.FileItem -> {
                    if (part.name == "coverImage") {
                        coverImage = part
                    } else images.add(part)
                }
                else -> Unit
            }
        }

        if (coverImage == null) {
            throw IllegalArgumentException("No cover image found")
        }

        if (name == null) {
            throw IllegalArgumentException("Name is null")
        }

        val coverPhotoUrl = azureBlobService.uploadImage(coverImage!!)

        val product = Product.new {
            this.name = name!!
            this.shortDescription = shortDescription!!
            this.fullDescription = fullDescription!!
            this.price = price!!
            this.discount = discount ?: 0.0
            this.stock = stock!!
            this.markAsNew = markAsNew ?: false
            this.coverPhotoUrl = coverPhotoUrl
        }

        val imageUrls: MutableList<String> = mutableListOf()

        images.forEach { image ->
            val imageUrl = azureBlobService.uploadImage(image)
            imageUrls.add(imageUrl)
        }

        return Pair(Pair(product, imageUrls), Pair(colors, sizes))
    }
}