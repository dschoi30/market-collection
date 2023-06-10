package com.marketcollection.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatus {
    ORDERED("주문 완료"), CANCELED("주문 취소"), FAILED("주문 실패");

    private final String status;
}
