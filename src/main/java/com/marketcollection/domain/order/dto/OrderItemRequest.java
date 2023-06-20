package com.marketcollection.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter @Setter
public class OrderItemRequest {
    private Long itemId;

    @Min(value = 1, message = "최소 1개 이상 주문해야 합니다.")
    private int count;
}
