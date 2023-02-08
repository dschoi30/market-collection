package com.marketcollection.domain.review;

import com.marketcollection.domain.common.BaseEntity;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Review extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reviewer;
    private String title;
    private String content;
    private int rating; // 리뷰 평점 1 ~ 5점
}
