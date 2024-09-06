package com.example.service.impl

import com.example.model.Feedback
import com.example.model.FeedbackDTO
import com.example.repository.FeedbackRepository
import com.example.service.FeedbackService
import java.util.*

class FeedbackServiceImpl(private val feedbackRepository: FeedbackRepository = FeedbackRepository()) : FeedbackService {
    override fun getAll(): List<FeedbackDTO> =
        feedbackRepository.getAll().map { it.toDto() }

    override fun addFeedback(feedback: FeedbackDTO) {
        feedbackRepository.addFeedback(feedback)
    }

    override fun archiveFeedback(feedbackId: UUID) {
        feedbackRepository.archiveFeedback(feedbackId)
    }

    private fun Feedback.toDto() =
        FeedbackDTO(
            id = this.id.value,
            clientName = this.clientName,
            email = this.email,
            subject = this.subject,
            message = this.message,
            isArchived = this.isArchived
        )
}
