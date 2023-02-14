package com.marketcollection.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemRequestDto {
    private Long itemId;
    private int count;
}
