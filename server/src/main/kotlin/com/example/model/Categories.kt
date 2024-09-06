package com.example.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import java.util.*

object Categories : BaseTable("categories") {
    val name = varchar("name", 256).uniqueIndex()
    val coverPhotoUrl = varchar("cover_photo_url", 256).nullable()
}

class Category(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Category>(Categories)

    var name by Categories.name
    var coverPhotoUrl by Categories.coverPhotoUrl
    var creationDate by Categories.creationDate
    var lastUpdatedDate by Categories.lastUpdatedDate
}

@Serializable
data class CategoryDTO(
    @Contextual val id: UUID? = null,
    val name: String,
    val coverPhotoUrl: String?
)

fun ResultRow.toCategory() = Category(
    id = this[Categories.id]
).apply {
    name = this@toCategory[Categories.name]
    coverPhotoUrl = this@toCategory[Categories.coverPhotoUrl]
    creationDate = this@toCategory[Categories.creationDate]
    lastUpdatedDate = this@toCategory[Categories.lastUpdatedDate]
}

fun ResultRow.toCategoryDTO() = CategoryDTO(
    id = this[Categories.id].value,
    name = this[Categories.name],
    coverPhotoUrl = this[Categories.coverPhotoUrl]
)