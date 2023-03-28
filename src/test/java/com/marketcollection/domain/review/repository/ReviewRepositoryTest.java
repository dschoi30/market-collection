package com.marketcollection.domain.review.repository;

import com.marketcollection.domain.common.Address;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.ItemSaleStatus;
import com.marketcollection.domain.item.dto.ItemFormDto;
import com.marketcollection.domain.item.repository.ItemRepository;
import com.marketcollection.domain.member.*;
import com.marketcollection.domain.member.repository.MemberRepository;
import com.marketcollection.domain.review.Review;
import com.marketcollection.domain.review.dto.ReviewDto;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class ReviewRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired ItemRepository itemRepository;
    @Autowired ReviewRepository reviewRepository;

    public Member saveMember() {
        Member member = new Member(1L, SocialType.NAVER, "test@gmail.com", "test1",
                "01012341234", new Address(), 0, Role.USER, Grade.NORMAL, MemberStatus.ACTIVE);
        return memberRepository.save(member);
    }
    public Item saveItem() {
        Item item = new Item("test1", 10000, 9000, 10000, "content",
                1L, "", 0, 0, 0, ItemSaleStatus.ON_SALE);
        return itemRepository.save(item);
    }

    @Test
    public void getReviewList() {
        Member member = saveMember();
        Item item = saveItem();

        ItemFormDto itemFormDto = ItemFormDto.of(item);
        ReviewDto reviewDto = new ReviewDto(itemFormDto);
        Review review = Review.createReview(reviewDto, item, member);
        reviewRepository.save(review);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        Page<Review> reviews = reviewRepository.getReviewList(item.getId(), pageable);

        reviews.getContent().forEach(r -> System.out.println("review===" + r));
    }

}