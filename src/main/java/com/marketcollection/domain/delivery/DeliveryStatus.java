package com.marketcollection.domain.delivery;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeliveryStatus {
    READY("상품 준비 중"),
    IN_PROGRESS("배송 중"),
    DONE("배송 완료");

    private String status;
}
