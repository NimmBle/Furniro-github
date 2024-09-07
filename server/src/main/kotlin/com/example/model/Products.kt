package com.example.model

import com.example.plugins.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import java.util.*

object Products : BaseTable("products") {
    val name = varchar("name", 512).uniqueIndex()
    val shortDescription = varchar("short_description", 512)
    val fullDescription = text("full_description")
    val price = double("price")
    val discount = double("discount").nullable()
    val stock = uinteger("stock")
    val markAsNew = bool("mark_as_new").nullable()
    val coverPhotoUrl = varchar("cover_photo_url", 512)
    val categoryId = reference("category_id", Categories.id)
}

class Product(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Product>(Products)

    var name by Products.name
    var shortDescription by Products.shortDescription
    var fullDescription by Products.fullDescription
    var price by Products.price
    var discount by Products.discount
    var stock by Products.stock
    var markAsNew by Products.markAsNew
    var coverPhotoUrl by Products.coverPhotoUrl
    var creationDate by Products.creationDate
    var lastUpdatedDate by Products.lastUpdatedDate
    var category by Category referencedOn Products.categoryId
}

object ProductPhotos : BaseTable("product_photos") {
    val productId = reference("product_id", Products.id)
    val photoUrl = varchar("photo_url", 512)
}

class ProductPhoto(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ProductPhoto>(ProductPhotos)

    var productId by ProductPhotos.productId
    var imageUrl by ProductPhotos.photoUrl
    var creationDate by ProductPhotos.creationDate
    var lastUpdatedDate by ProductPhotos.lastUpdatedDate
}

enum class ProductSizeEnum {
    S, M, L, XL, XXL
}

object ProductSizes : BaseTable("product_sizes") {
    val productId = reference("product_id", Products.id)
    val size = enumerationByName("size", 2, ProductSizeEnum::class)
}

class ProductSize(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ProductSize>(ProductSizes)

    var productId by ProductSizes.productId
    var size by ProductSizes.size
    var creationDate by ProductSizes.creationDate
    var lastUpdatedDate by ProductSizes.lastUpdatedDate
}

object ProductColors : BaseTable("product_colors") {
    val productId = reference("product_id", Products.id)
    val name = varchar("name", 256).uniqueIndex()
}

class ProductColor(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ProductColor>(ProductColors)

    var productId by ProductColors.productId
    var name by ProductColors.name
    var creationDate by ProductColors.creationDate
    var lastUpdatedDate by ProductColors.lastUpdatedDate
}

@Serializable
data class ProductDTO(
    @Serializable(with = UUIDSerializer::class) val id: UUID? = null,
    @Serializable(with = UUIDSerializer::class) val categoryId: UUID? = null,
    val name: String,
    val shortDescription: String,
    val fullDescription: String,
    val price: Double,
    val discount: Double?,
    val stock: UInt,
    val markAsNew: Boolean?,
    val coverPhotoUrl: String,
    val photos: MutableList<String>,
    val sizes: MutableList<String>,
    val colors: MutableList<String>
)

@Serializable
data class ProductColorDTO(
    @Contextual val id: UUID? = null,
    @Contextual val productId: UUID,
    val name: String
)

@Serializable
data class ProductSizeDTO(
    @Contextual val id: UUID? = null,
    @Contextual val productId: UUID,
    val size: String
)

@Serializable
data class ProductPhotoDTO(
    @Contextual val id: UUID? = null,
    @Contextual val productId: UUID,
    val imageUrl: String
)

fun ResultRow.toProduct() = Product(
    id = this[Products.id]
).apply {
    name = this@toProduct[Products.name]
    shortDescription = this@toProduct[Products.shortDescription]
    fullDescription = this@toProduct[Products.fullDescription]
    price = this@toProduct[Products.price]
    discount = this@toProduct[Products.discount]
    stock = this@toProduct[Products.stock]
    markAsNew = this@toProduct[Products.markAsNew]
    coverPhotoUrl = this@toProduct[Products.coverPhotoUrl]
    creationDate = this@toProduct[Products.creationDate]
    lastUpdatedDate = this@toProduct[Products.lastUpdatedDate]
    category = Category.findById(this@toProduct[Products.categoryId]) ?: throw IllegalArgumentException("Category not found")
}

fun ResultRow.toProductDTO() = ProductDTO(
    id = this[Products.id].value,
    categoryId = this[Products.categoryId].value,
    name = this[Products.name],
    shortDescription = this[Products.shortDescription],
    fullDescription = this[Products.fullDescription],
    price = this[Products.price],
    discount = this[Products.discount],
    stock = this[Products.stock],
    markAsNew = this[Products.markAsNew],
    coverPhotoUrl = this[Products.coverPhotoUrl],
    photos = mutableListOf(),
    sizes = mutableListOf(),
    colors = mutableListOf()
)

fun ResultRow.toProductColor() = ProductColor(
    id = this[ProductColors.id]
).apply {
    productId = this@toProductColor[ProductColors.productId]
    name = this@toProductColor[ProductColors.name]
}

fun ResultRow.toProductSize() = ProductSize(
    id = this[ProductSizes.id]
).apply {
    productId = this@toProductSize[ProductSizes.productId]
    size = this@toProductSize[ProductSizes.size]
}

fun ResultRow.toProductPhoto() = ProductPhoto(
    id = this[ProductPhotos.id]
).apply {
    productId = this@toProductPhoto[ProductPhotos.productId]
    imageUrl = this@toProductPhoto[ProductPhotos.photoUrl]
}