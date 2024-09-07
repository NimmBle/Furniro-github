package com.example.model

import com.example.plugins.UUIDSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

object Feedbacks : BaseTable("feedbacks") {
    val clientName = varchar("client_name", 256)
    val email = varchar("email", 256)
    val subject = varchar("subject", 256)
    val message = text("message")
    val isArchived = bool("is_archived")
}

class Feedback(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Feedback>(Feedbacks)

    var clientName by Feedbacks.clientName
    var email by Feedbacks.email
    var subject by Feedbacks.subject
    var message by Feedbacks.message
    var isArchived by Feedbacks.isArchived
    var creationDate by Feedbacks.creationDate
    var lastUpdatedDate by Feedbacks.lastUpdatedDate
}

@Serializable
data class FeedbackDTO(
    @Serializable(with = UUIDSerializer::class) val id: UUID? = null,
    val clientName: String,
    val email: String,
    val subject: String,
    val message: String,
    val isArchived: Boolean
)