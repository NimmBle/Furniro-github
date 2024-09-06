package com.example.model

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
}

object ProductPhotos : BaseTable("product_photos") {
    val productId = reference("product_id", Products.id)
    val photoUrl = varchar("photo_url", 512)
}

class ProductPhoto(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ProductPhoto>(ProductPhotos)

    var productId by ProductPhotos.productId
    var photoUrl by ProductPhotos.photoUrl
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
}

object ProductColors : BaseTable("colors") {
    val productId = reference("product_id", Products.id)
    val name = varchar("name", 256).uniqueIndex()
}

class ProductColor(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ProductColor>(ProductColors)

    var productId by ProductColors.productId
    var name by ProductColors.name
}

@Serializable
data class ProductDTO(
    @Contextual val id: UUID? = null,
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
}

fun ResultRow.toProductDTO() = ProductDTO(
    id = this[Products.id].value,
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