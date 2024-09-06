package com.example.service.impl

import com.example.model.Review
import com.example.model.ReviewDTO
import com.example.repository.ReviewRepository
import com.example.service.ReviewService
import java.util.*

class ReviewServiceImpl(private val reviewRepository: ReviewRepository = ReviewRepository()) : ReviewService {
    override fun getByProductId(productId: UUID): List<ReviewDTO> =
        reviewRepository.getReviewsByProductId(productId).map { it.toDto() }

    override fun addReview(reviewDTO: ReviewDTO) {
        reviewRepository.addReview(reviewDTO)
    }

    private fun Review.toDto() =
        ReviewDTO(
            productId = product.id.value,
            rating = rating,
            comment = comment
        )
}