package com.marketcollection.domain.review.repository;

import com.marketcollection.domain.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select r from Review r where r.item.id = :id")
    Page<Review> getReviewList(@Param("id") Long itemId, Pageable pageable);

    @Query("select count(r) from Review r where r.item.id = :id")
    Long countReview(@Param("id") Long itemId);
}
