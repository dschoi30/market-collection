package com.marketcollection.domain.item.dto;

import com.marketcollection.domain.item.Category;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.ItemSaleStatus;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
@Getter @Setter
public class ItemFormDto {
    private Long id;

    @NotBlank @Size(max = 255, message = "상품명은 필수 입력 값입니다.")
    private String itemName;

    @Min(value = 1, message = "상품 가격은 필수 입력 값입니다.")
    private int originalPrice;

    @Min(value = 1, message = "상품 가격은 필수 입력 값입니다.")
    private int salePrice;

    @Min(value = 1, message = "상품 판매 수량은 필수 입력 값입니다.")
    private int stockQuantity;

    @NotBlank @Size(max = 1000, message = "상품 설명은 필수 입력 값입니다.")
    private String description;

    @NotNull(message = "카테고리는 필수 입력 값입니다.")
    private Long categoryId;

    private String repImageUrl;

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
                .categoryId(categoryId)
                .itemSaleStatus(itemSaleStatus)
                .build();
    }

    private static ModelMapper modelMapper = new ModelMapper();
    public static ItemFormDto of(Item item) { return modelMapper.map(item, ItemFormDto.class);
    }
}
