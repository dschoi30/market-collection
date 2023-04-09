package com.marketcollection.domain.category.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class ItemCategoryDto implements Serializable {
    private static final long serialVersionUID = 6923777867934367894L;

    private Long id;
    private String categoryName;
    private Long parentId;
    private List<ItemCategoryDto> subCategories;

    public ItemCategoryDto(Long id, String categoryName, Long parentId) {
        this.id = id;
        this.categoryName = categoryName;
        this.parentId = parentId;
    }

    public String findCategoryName(Long id) {

        for (ItemCategoryDto level1 : subCategories) {
            List<ItemCategoryDto> level2 = level1.getSubCategories();

            for (ItemCategoryDto itemCategoryDto : level2) {
                if (Objects.equals(itemCategoryDto.getId(), id)) {
                    return itemCategoryDto.getCategoryName();
                }
            }
        }
        return "";
    }
}
