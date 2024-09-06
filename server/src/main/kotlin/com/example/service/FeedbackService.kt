package com.example.service

import com.example.model.FeedbackDTO
import java.util.*

interface FeedbackService {

    fun getAll(): List<FeedbackDTO>

    fun addFeedback(feedback: FeedbackDTO)

    fun archiveFeedback(feedbackId: UUID)
}