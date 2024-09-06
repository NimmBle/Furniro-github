package com.example.model

import com.example.plugins.UUIDSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

object Reviews : BaseTable("reviews") {
    val productId = reference("product_id", Products)
    val rating = ubyte("rating")
    val comment = text("comment")
}

class Review(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Review>(Reviews)

    var product by Product referencedOn Reviews.productId
    var rating by Reviews.rating
    var comment by Reviews.comment
    var creationDate by Reviews.creationDate
    var lastUpdatedDate by Reviews.lastUpdatedDate
}

@Serializable
data class ReviewDTO(
    @Serializable(with = UUIDSerializer::class) val productId: UUID? = null,
    val rating: UByte,
    val comment: String
)