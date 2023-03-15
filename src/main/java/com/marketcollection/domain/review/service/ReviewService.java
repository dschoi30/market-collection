package com.marketcollection.domain.review.service;

import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.repository.ItemRepository;
import com.marketcollection.domain.member.Member;
import com.marketcollection.domain.member.repository.MemberRepository;
import com.marketcollection.domain.review.Review;
import com.marketcollection.domain.review.dto.PageRequestDto;
import com.marketcollection.domain.review.dto.PageResponseDto;
import com.marketcollection.domain.review.dto.ReviewDto;
import com.marketcollection.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    public Long save(String memberId, ReviewDto reviewDto) {

        Item item = itemRepository.findById(reviewDto.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
        Member member = memberRepository.findByEmail(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        Review review = Review.createReview(reviewDto, item, member);

        reviewRepository.save(review);
        item.addReviewCount();

        return review.getId();
    }

    public PageResponseDto<ReviewDto> getReviewList(Long itemId, PageRequestDto pageRequestDto) {
        Pageable pageable = PageRequest.of(pageRequestDto.getPage() <= 0 ? 0 : pageRequestDto.getPage() - 1, pageRequestDto.getSize(),
                Sort.by("id").descending());

        Page<Review> reviews = reviewRepository.getReviewList(itemId, pageable);
        List<ReviewDto> reviewDtos = reviews.getContent().stream()
                .map(review -> review.toDto(review)).collect(Collectors.toList());

        return PageResponseDto.<ReviewDto>withAll()
                .pageRequestDto(pageRequestDto)
                .dtoList(reviewDtos)
                .total((int) reviews.getTotalElements())
                .build();
    }

}
