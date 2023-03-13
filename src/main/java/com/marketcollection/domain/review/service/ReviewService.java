package com.marketcollection.domain.review.service;

import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.repository.ItemRepository;
import com.marketcollection.domain.member.Member;
import com.marketcollection.domain.member.repository.MemberRepository;
import com.marketcollection.domain.review.Review;
import com.marketcollection.domain.review.dto.ReviewFormDto;
import com.marketcollection.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    public Long save(String memberId, ReviewFormDto reviewFormDto) {

        Item item = itemRepository.findById(reviewFormDto.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
        Member member = memberRepository.findByEmail(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        Review review = Review.createReview(reviewFormDto, item, member);

        reviewRepository.save(review);
        item.addReviewCount();

        return review.getId();
    }
}
