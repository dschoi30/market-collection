package com.marketcollection.api.item.dto;

import com.marketcollection.domain.item.Category;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.ItemSaleStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemFormDto {

    private String itemName;
    private int originalPrice;
    private int salePrice;
    private int stockQuantity;
    private String description;
    private String thumbnailImageFile;
    private Category category;
    private ItemSaleStatus itemSaleStatus;

    public Item toEntity() {
        return Item.builder()
                .itemName(itemName)
                .originalPrice(originalPrice)
                .salePrice(salePrice)
                .stockQuantity(stockQuantity)
                .description(description)
                .thumbnailImageFile(thumbnailImageFile)
                .category(category)
                .itemSaleStatus(itemSaleStatus)
                .build();
    }
}
