package com.marketcollection.domain.item.dto;

import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.ItemSaleStatus;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.NumberFormat;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ItemDetailDto {

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

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemDetailDto of(Item item) {
        return modelMapper.map(item, ItemDetailDto.class);
    }
}
