package com.marketcollection.domain.payment.dto;

import lombok.Getter;

@Getter
public class PaymentResponseDto {

    private int totalAmount;

    public PaymentResponseDto(int totalAmount) {
        this.totalAmount = totalAmount;
    }
}
