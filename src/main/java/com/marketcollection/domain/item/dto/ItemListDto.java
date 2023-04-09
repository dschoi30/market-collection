package com.marketcollection.domain.item.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemListDto {

    private Long id;
    private String itemName;
    private int originalPrice;
    private int salePrice;
    private String imageUrl;
    private int reviewCount;

    @QueryProjection
    public ItemListDto(Long id, String itemName, int originalPrice,
                       int salePrice, String imageUrl, int reviewCount) {
        this.id = id;
        this.itemName = itemName;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.imageUrl = imageUrl;
        this.reviewCount = reviewCount;
    }

    @QueryProjection
    public ItemListDto(Long id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }
}
