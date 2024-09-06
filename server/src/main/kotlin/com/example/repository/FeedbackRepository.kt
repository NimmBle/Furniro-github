package com.example.repository

import com.example.model.Feedback
import com.example.model.FeedbackDTO
import com.example.model.Feedbacks
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDate
import java.util.UUID

class FeedbackRepository {

    fun getAll(): List<Feedback> = transaction {
        Feedback.all().toList()
    }

    fun addFeedback(feedback: FeedbackDTO) = transaction {
        Feedback.new {
            this.clientName = feedback.clientName
            this.email = feedback.email
            this.subject = feedback.subject
            this.message = feedback.message
            this.isArchived = false
            this.creationDate = LocalDate.now()
            this.lastUpdatedDate = LocalDate.now()
        }
    }

    fun archiveFeedback(feedbackId: UUID) = transaction {
        Feedbacks.update({ Feedbacks.id eq feedbackId }) {
            it[isArchived] = true
            it[lastUpdatedDate] = LocalDate.now()
        }
    }
}