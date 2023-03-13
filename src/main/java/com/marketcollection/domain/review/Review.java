package com.marketcollection.domain.review;

import com.marketcollection.domain.common.BaseEntity;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.member.Member;
import com.marketcollection.domain.review.dto.ReviewFormDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Review extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String content;
    private int recommendCount;

    public static Review createReview(ReviewFormDto reviewFormDto, Item item, Member member) {
        return Review.builder()
                .item(item)
                .member(member)
                .content(reviewFormDto.getContent())
                .build();
    }
}
