package com.marketcollection.domain.category.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ItemCategoryDto {
    private Long id;
    private String categoryName;
    private Long parentId;
    private List<ItemCategoryDto> subCategories;

    public ItemCategoryDto(Long id, String categoryName, Long parentId) {
        this.id = id;
        this.categoryName = categoryName;
        this.parentId = parentId;
    }
}
