package com.marketcollection.domain.discount.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DailySaleItemListDto {
    private Long itemDiscountId;
    private Long itemId;
    private String itemName;
    private int originalPrice;
    private int salePrice;
    private int discountPrice;
    private String imageUrl;
    private int reviewCount;
    private LocalDateTime dueDate;

    @QueryProjection
    public DailySaleItemListDto(Long itemDiscountId, Long itemId, String itemName, int originalPrice, int salePrice, int discountPrice,
                                String imageUrl, int reviewCount, LocalDateTime dueDate) {
        this.itemDiscountId = itemDiscountId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.discountPrice = discountPrice;
        this.imageUrl = imageUrl;
        this.reviewCount = reviewCount;
        this.dueDate = dueDate;
    }
}
