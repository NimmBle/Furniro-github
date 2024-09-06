package com.example.service

import com.example.model.ReviewDTO
import java.util.UUID

interface ReviewService {

    fun getByProductId(productId: UUID): List<ReviewDTO>

    fun addReview(reviewDTO: ReviewDTO)
}