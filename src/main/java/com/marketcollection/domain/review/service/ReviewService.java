package com.marketcollection.domain.review.service;

import com.marketcollection.common.exception.ErrorCode;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.repository.ItemRepository;
import com.marketcollection.domain.member.Member;
import com.marketcollection.domain.member.repository.MemberRepository;
import com.marketcollection.domain.reaction.Reaction;
import com.marketcollection.domain.reaction.ReactionRepository;
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

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final ReactionRepository reactionRepository;

    public Long saveReview(String memberId, ReviewDto reviewDto) {

        Item item = itemRepository.findById(reviewDto.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
        Member member = memberRepository.findByEmail(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        Review review = Review.createReview(reviewDto, item, member);

        reviewRepository.save(review);
        item.addReviewCount();

        return review.getId();
    }

    @Transactional(readOnly = true)
    public PageResponseDto<ReviewDto> getReviewList(Long itemId, PageRequestDto pageRequestDto) {
        Pageable pageable = PageRequest.of(pageRequestDto.getPage() <= 0 ? 0 : pageRequestDto.getPage() - 1, pageRequestDto.getSize(),
                Sort.by("id").descending());

        Page<Review> reviews = reviewRepository.getReviewList(itemId, pageable);
        List<ReviewDto> reviewDtos = reviews.stream()
                .map(review -> review.toDto(review)).collect(Collectors.toList());

        return PageResponseDto.<ReviewDto>builder()
                .pageRequestDto(pageRequestDto)
                .dtoList(reviewDtos)
                .total((int) reviews.getTotalElements())
                .build();
    }

    public int updateReaction(String memberId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(EntityNotFoundException::new);
        Optional<Reaction> savedReaction = reactionRepository.findByMemberIdAndReviewId(memberId, reviewId);
        if(savedReaction.isEmpty()) {
            Reaction reaction = Reaction.createReaction(review, memberId);
            reactionRepository.save(reaction);
            review.addLikes();
        } else {
            reactionRepository.delete(savedReaction.get());
            review.deleteLikes();
        }

        return review.getLikes();
    }
}
