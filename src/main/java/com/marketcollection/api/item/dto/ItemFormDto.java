package com.marketcollection.api.item.dto;

import com.marketcollection.domain.item.Category;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.ItemSaleStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ItemFormDto {

    private Long id;
    private String itemName;
    private int originalPrice;
    private int salePrice;
    private int stockQuantity;
    private String description;
    private String thumbnailImageFile;
    private Long categoryId;
    private ItemSaleStatus itemSaleStatus;
    private List<ItemImageDto> itemImageDtos = new ArrayList<>();

    public Item toEntity() {
        return Item.builder()
                .itemName(itemName)
                .originalPrice(originalPrice)
                .salePrice(salePrice)
                .stockQuantity(stockQuantity)
                .description(description)
                .thumbnailImageFile(thumbnailImageFile)
                .categoryId(categoryId)
                .itemSaleStatus(itemSaleStatus)
                .build();
    }
}
