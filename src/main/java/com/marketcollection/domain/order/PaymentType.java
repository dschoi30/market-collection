package com.marketcollection.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentType {
    CARD("카드"),
    TRANSFER("계좌이체"),
    POINT("포인트");

    private final String type;
}
