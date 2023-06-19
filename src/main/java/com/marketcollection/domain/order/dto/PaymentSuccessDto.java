package com.marketcollection.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentSuccessDto {
    private String orderNumber;
    private int totalAmount;

    public static PaymentSuccessDto of(PGResponseDto tossPaymentDto) {
        return PaymentSuccessDto.builder()
                .orderNumber(tossPaymentDto.getOrderId())
                .totalAmount(tossPaymentDto.getTotalAmount())
                .build();
    }
}
