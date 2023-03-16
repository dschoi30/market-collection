package com.marketcollection.domain.review;

import com.marketcollection.domain.common.BaseTimeEntity;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.member.Member;
import com.marketcollection.domain.review.dto.ReviewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Review extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String content;
    private int likes;

    public static Review createReview(ReviewDto reviewDto, Item item, Member member) {
        return Review.builder()
                .item(item)
                .member(member)
                .content(reviewDto.getContent())
                .build();
    }

    public ReviewDto toDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .itemId(review.getItem().getId())
                .itemName(review.getItem().getItemName())
                .memberName(review.getMember().getMemberName())
                .memberGrade(review.getMember().getGrade().getTitle())
                .repImageUrl(review.getItem().getRepImageUrl())
                .likes(review.getLikes())
                .content(review.getContent())
                .createdDate(review.getCreatedDate())
                .build();
    }

    public void addLikes() {
        likes++;
    }

    public void deleteLikes() { likes--; }
}
