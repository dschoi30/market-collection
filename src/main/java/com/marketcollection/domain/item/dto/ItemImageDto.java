package com.marketcollection.domain.item.dto;

import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.ItemImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
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

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImageDto of(ItemImage itemImage) {
        return modelMapper.map(itemImage, ItemImageDto.class);
    }
}
