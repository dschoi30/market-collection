package com.marketcollection.domain.order.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PaymentCancelDto {
    private Long orderId;
    private List<Long> orderItemIds;
    private String cancelReason;
}
