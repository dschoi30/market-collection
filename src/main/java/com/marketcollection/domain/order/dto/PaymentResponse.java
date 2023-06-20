package com.marketcollection.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentResponse {
    private String orderNumber;
    private int totalAmount;

    public static PaymentResponse of(PGResponse tossPaymentDto) {
        return PaymentResponse.builder()
                .orderNumber(tossPaymentDto.getOrderId())
                .totalAmount(tossPaymentDto.getTotalAmount())
                .build();
    }
}
