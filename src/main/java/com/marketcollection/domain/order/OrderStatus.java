package com.marketcollection.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatus {
    READY("배송 준비 중"), COMPLETED("배송 완료");

    private final String status;
}
