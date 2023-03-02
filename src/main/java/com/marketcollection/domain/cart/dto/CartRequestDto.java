package com.marketcollection.domain.cart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartRequestDto {
    private Long itemId;
    private int count;

    public CartRequestDto(Long itemId, int count) {
        this.itemId = itemId;
        this.count = count;
    }
}
