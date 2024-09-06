package com.example.service.impl.product

import com.example.model.Category
import com.example.model.Product
import com.example.model.ProductDTO
import com.example.repository.product.ProductRepository
import com.example.service.*
import com.example.service.product.ProductColorService
import com.example.service.product.ProductPhotoService
import com.example.service.product.ProductService
import com.example.service.product.ProductSizeService
import io.ktor.http.content.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class ProductServiceImpl(
    private val productRepository: ProductRepository = ProductRepository(),
    private val productPhotoService: ProductPhotoService = ProductPhotoServiceImpl(),
    private val productSizeService: ProductSizeService = ProductSizeServiceImpl(),
    private val productColorService: ProductColorService = ProductColorServiceImpl(),
    private val azureBlobService: AzureBlobService,
    private val categoryService: CategoryService,
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

    override fun getPagination(page: Int, size: Long): List<ProductDTO> =
        productRepository
            .getPagination(page, size)
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

        val productId =
            (productRepository.getByProductName(product.name) ?: throw NullPointerException("Product is null")).id.value

        colors.forEach {
            productColorService.addProductColor(productId, it)
        }
        sizes.forEach {
            productSizeService.addProductSize(productId, it)
        }
        imageUrls.forEach {
            productPhotoService.addProductPhoto(productId, it)
        }
    }

    override suspend fun editProduct(oldName: String, multipart: MultiPartData) {
        val existingProduct = productRepository.getByProductName(oldName)
            ?: throw IllegalArgumentException("Product with name $oldName does not exist")

        val (first, second) = uploadImages(multipart)
        val (product, imageUrls) = first
        val (colors, sizes) = second

        val productId = existingProduct.id.value

        colors.forEach {
            val colorsCurr = productColorService.getByProductId(productId)

            colorsCurr.forEach { curr ->
                if (!colors.contains(curr)) {
                    productColorService.deleteProductColor(productId, curr)
                }
            }

            if (!colorsCurr.contains(it)) {
                productColorService.addProductColor(productId, it)
            }
        }
        sizes.forEach {
            val sizesCurr = productSizeService.getByProductId(productId)
            if (!sizesCurr.contains(it)) {
                productSizeService.addProductSize(productId, it)
            }

            sizesCurr.forEach { curr ->
                if (!sizes.contains(curr)) {
                    productSizeService.deleteProductSize(productId, curr)
                }
            }
            productSizeService.addProductSize(productId, it)
        }
        imageUrls.forEach {
            productPhotoService.addProductPhoto(productId, it)
        }

        existingProduct.apply {
            name = product.name
            category = Category.findById(category.id) ?: throw IllegalArgumentException("Category not found")
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

    private fun Product.toDTO() = transaction {
        ProductDTO(
            id = UUID.fromString(id),
            name = name,
            shortDescription = shortDescription,
            fullDescription = fullDescription,
            price = price,
            discount = discount,
            stock = stock,
            markAsNew = markAsNew,
            coverPhotoUrl = coverPhotoUrl,
            categoryId = category.id.value,
            photos = mutableListOf(),
            sizes = mutableListOf(),
            colors = mutableListOf()
        )
    }

    private suspend fun uploadImages(multipart: MultiPartData): Pair<Pair<ProductDTO, MutableList<String>>, Pair<List<String>, List<String>>> {

        var name: String? = null
        var shortDescription: String? = null
        var fullDescription: String? = null
        var price: Double? = null
        var discount: Double? = null
        var stock: UInt? = null
        var markAsNew: Boolean? = null

        val colors: MutableList<String> = mutableListOf()
        val sizes: MutableList<String> = mutableListOf()

        var categoryName: String? = null

        val images: MutableList<PartData.FileItem> = mutableListOf()
        var coverImage: PartData.FileItem? = null

        multipart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    when (part.name) {
                        "name" -> name = part.value
                        "category" -> categoryName = part.value
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

        if (categoryName == null) {
            throw IllegalArgumentException("Category is null")
        }

        val coverPhotoUrl = azureBlobService.uploadImage(coverImage!!)

        val categoryId = categoryService.getByName(categoryName!!).id

        val product = ProductDTO(
            name = name!!,
            shortDescription = shortDescription!!,
            fullDescription = fullDescription!!,
            price = price!!,
            discount = discount ?: 0.0,
            stock = stock!!,
            markAsNew = markAsNew ?: false,
            coverPhotoUrl = coverPhotoUrl,
            categoryId = categoryId,
            photos = mutableListOf(),
            sizes = mutableListOf(),
            colors = mutableListOf()
        )

        val imageUrls: MutableList<String> = mutableListOf()

        images.forEach { image ->
            val imageUrl = azureBlobService.uploadImage(image)
            imageUrls.add(imageUrl)
        }

        return Pair(Pair(product, imageUrls), Pair(colors, sizes))
    }
}