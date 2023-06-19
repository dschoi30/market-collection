package com.marketcollection.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderCancelDto {
    private Long orderId;
    private List<Long> orderItemIds;
    private String cancelReason;

    public OrderCancelDto(Long orderId, String cancelReason) {
        this.orderId = orderId;
        this.cancelReason = cancelReason;
    }
}
