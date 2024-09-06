package com.example.repository.product

import com.example.model.ProductPhoto
import com.example.model.ProductPhotos
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.util.*

class ProductPhotoRepository {

    fun addProductPhoto(productId: UUID, photoUrl: String) = transaction {
        ProductPhoto.new {
            this.productId = EntityID(productId, ProductPhotos)
            this.imageUrl = photoUrl
            this.creationDate = LocalDate.now()
            this.lastUpdatedDate = LocalDate.now()
        }
    }

    fun deleteProductPhoto(productId: UUID, photoUrl: String) = transaction {
        ProductPhotos.deleteWhere { ProductPhotos.productId eq productId and (ProductPhotos.photoUrl eq photoUrl) }
    }

    fun getByProductId(productId: UUID): List<ProductPhoto> = transaction {
        ProductPhoto.find { ProductPhotos.productId eq productId }.toList()
    }
}