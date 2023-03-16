package com.marketcollection.domain.reaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Reaction findByReviewId(Long reviewId);

    Reaction findByMemberId(String memberId);

    @Query("select r from Reaction r where r.memberId =:memberId and r.review.id =:reviewId")
    Optional<Reaction> findByMemberIdAndReviewId(@Param("memberId") String memberId, @Param("reviewId") Long reviewId);
}
