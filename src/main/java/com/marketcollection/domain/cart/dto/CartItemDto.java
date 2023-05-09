package com.marketcollection.domain.cart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartItemDto {
    private Long cartItemId;
    private String imageUrl;
    private Long itemId;
    private String itemName;
    private int originalPrice;
    private int salePrice;
    private int discountPrice;
    private int count;

    public CartItemDto(Long cartItemId, String imageUrl, Long itemId, String itemName, int originalPrice, int salePrice, int discountPrice, int count) {
        this.cartItemId = cartItemId;
        this.imageUrl = imageUrl;
        this.itemId = itemId;
        this.itemName = itemName;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.discountPrice = discountPrice;
        this.count = count;
    }
}
