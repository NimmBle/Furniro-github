package com.example.repository

import com.example.model.Product
import com.example.model.Review
import com.example.model.ReviewDTO
import com.example.model.Reviews
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ReviewRepository {

    fun getReviewsByProductId(productId: UUID): List<Review> = transaction {
        Review.find { Reviews.productId eq productId }.toList()
    }

    fun addReview(reviewDTO: ReviewDTO) = transaction {
        Review.new {
            this.product = Product.findById(reviewDTO.productId
                ?: throw IllegalArgumentException("Product ID is required"))
                ?: throw IllegalArgumentException("Product not found")
            this.rating = reviewDTO.rating
            this.comment = reviewDTO.comment
        }
    }
}