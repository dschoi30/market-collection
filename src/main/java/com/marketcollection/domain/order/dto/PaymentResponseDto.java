package com.marketcollection.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentResponseDto {
    private String orderNumber;
    private int totalAmount;

    public static PaymentResponseDto of(TossPaymentDto tossPaymentDto) {
        return PaymentResponseDto.builder()
                .orderNumber(tossPaymentDto.getOrderId())
                .totalAmount(Integer.parseInt(tossPaymentDto.getTotalAmount()))
                .build();
    }
}
