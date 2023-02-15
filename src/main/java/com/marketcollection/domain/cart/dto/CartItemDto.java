package com.marketcollection.domain.cart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartItemDto {
    private Long cartItemId;
    private Long itemId;
    private String imageUrl;
    private String itemName;
    private int originalPrice;
    private int salePrice;
    private int count;
}
