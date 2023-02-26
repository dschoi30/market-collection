package com.marketcollection.domain.item.dto;

import com.marketcollection.domain.item.Category;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.ItemSaleStatus;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

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
    private Category category;
    private ItemSaleStatus itemSaleStatus;
    private List<ItemImageDto> itemImageDtos = new ArrayList<>();
    private List<Long> itemImageIds = new ArrayList<>();

    public Item toEntity() {
        return Item.builder()
                .itemName(itemName)
                .originalPrice(originalPrice)
                .salePrice(salePrice)
                .stockQuantity(stockQuantity)
                .description(description)
                .category(category)
                .itemSaleStatus(itemSaleStatus)
                .build();
    }

    private static ModelMapper modelMapper = new ModelMapper();
    public static ItemFormDto of(Item item) { return modelMapper.map(item, ItemFormDto.class);
    }
}
