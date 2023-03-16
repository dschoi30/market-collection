package com.marketcollection.domain.reaction;

import com.marketcollection.domain.common.BaseTimeEntity;
import com.marketcollection.domain.review.Review;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Reaction extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    private String memberId;
    private int likes;

    public static Reaction createReaction(Review review, String memberId) {
        return Reaction.builder()
                .review(review)
                .memberId(memberId)
                .build();
    }
}
