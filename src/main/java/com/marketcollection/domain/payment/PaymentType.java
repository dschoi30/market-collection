package com.marketcollection.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentType {
    CARD("카드"),
    TRANSFER("계좌이체"),
    EASY_PAY("간편결제"),
    POINT("포인트");

    private final String type;
}
