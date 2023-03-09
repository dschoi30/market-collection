package com.marketcollection.domain.point;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EventType {

    ORDER("주문 적립"), REVIEW("리뷰 적립"),
    CANCELED("주문 취소"), EXPIRED("유효기간 만료");

    private final String title;
}
