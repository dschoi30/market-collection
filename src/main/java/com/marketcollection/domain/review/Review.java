package com.marketcollection.domain.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reviewer;
    private String title;
    private String content;
    private int rating; // 리뷰 평점 1 ~ 5점
}
