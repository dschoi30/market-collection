package com.marketcollection.api.item.dto;

import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.ItemImage;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemImageDto {

    private Long id;
    private Item item;
    private String originalFileName;
    private String renamedFileName;
    private String itemImageUrl;

    public void createItemImage(String originalFilename, String renamedFileName, String itemImageUrl) {
        this.originalFileName = originalFilename;
        this.renamedFileName = renamedFileName;
        this.itemImageUrl = itemImageUrl;
    }

    public ItemImage toEntity() {
        return ItemImage.builder()
                .item(item)
                .originalFileName(originalFileName)
                .renamedFileName(renamedFileName)
                .itemImageUrl(itemImageUrl)
                .build();

    }
}
