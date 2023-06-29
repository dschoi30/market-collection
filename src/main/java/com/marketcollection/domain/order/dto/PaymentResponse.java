package com.marketcollection.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentResponse {
    private String orderNumber;
    private int totalAmount;
    private Long orderId;

    public static PaymentResponse of(PGResponse tossPaymentDto, Long orderId) {
        return PaymentResponse.builder()
                .orderNumber(tossPaymentDto.getOrderId())
                .totalAmount(tossPaymentDto.getTotalAmount())
                .orderId(orderId)
                .build();
    }
}
