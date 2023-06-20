package com.marketcollection.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatus {
    READY("상품 준비 중"),
    IN_PROGRESS("배송 중"),
    DONE("배송 완료"),
    CANCELED("주문 취소"),
    PARTIAL_CANCELED("부분 취소"),
    ABORTED("주문 실패");

    private final String status;
}
