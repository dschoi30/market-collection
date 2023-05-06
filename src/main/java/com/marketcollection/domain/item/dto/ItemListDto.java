package com.marketcollection.domain.item.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class ItemListDto implements Serializable {
    private static final long serialVersionUID = -7789795624713666618L;

    private Long id;
    private String itemName;
    private int originalPrice;
    private int salePrice;
    private int discountPrice;
    private String imageUrl;
    private int reviewCount;

    @QueryProjection
    public ItemListDto(Long id, String itemName, int originalPrice,
                       int salePrice, int discountPrice, String imageUrl, int reviewCount) {
        this.id = id;
        this.itemName = itemName;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.discountPrice = discountPrice;
        this.imageUrl = imageUrl;
        this.reviewCount = reviewCount;
    }

    @QueryProjection
    public ItemListDto(Long id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }
}
