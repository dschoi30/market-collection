package com.marketcollection.api.item.dto;

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

    @QueryProjection
    public ItemListDto(Long id, String itemName, int originalPrice, int salePrice, String imageUrl) {
        this.id = id;
        this.itemName = itemName;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.imageUrl = imageUrl;
    }
}
