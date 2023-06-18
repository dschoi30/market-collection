package com.marketcollection.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentStatus {
    READY("결제 대기"),
    IN_PROGRESS("결제 승인 대기"),
    WAITING_FOR_DEPOSIT("가상계좌 입금 대기"),
    DONE("주문 완료"),
    CANCELED("주문 취소"),
    PARTIAL_CANCELED("부분 취소"),
    ABORTED("결제 실패"),
    EXPIRED("결제 만료");

    private final String status;
}
