package com.marketcollection.domain.review.repository;

import com.marketcollection.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
