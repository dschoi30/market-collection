package com.marketcollection.domain.discount.dto;

import com.marketcollection.domain.discount.DiscountStatus;
import com.marketcollection.domain.discount.DiscountType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DiscountResponseDto {
    private Long itemDiscountId;
    private DiscountStatus discountStatus;
    private DiscountType discountType;
    private Long itemId;
    private String repImageUrl;
    private String itemName;
    private int originalSalePrice;
    private int discountPrice;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;

    @QueryProjection
    public DiscountResponseDto(Long itemDiscountId, DiscountStatus discountStatus, DiscountType discountType,
                               Long itemId, String repImageUrl, String itemName, int originalSalePrice, int discountPrice,
                               LocalDateTime startDate, LocalDateTime finishDate) {
        this.itemDiscountId = itemDiscountId;
        this.discountStatus = discountStatus;
        this.discountType = discountType;
        this.itemId = itemId;
        this.repImageUrl = repImageUrl;
        this.itemName = itemName;
        this.originalSalePrice = originalSalePrice;
        this.discountPrice = discountPrice;
        this.startDate = startDate;
        this.finishDate = finishDate;
    }
}
