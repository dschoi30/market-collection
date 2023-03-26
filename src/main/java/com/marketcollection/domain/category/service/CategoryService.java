package com.marketcollection.domain.category.service;

import com.marketcollection.domain.category.ItemCategory;
import com.marketcollection.domain.category.dto.ItemCategoryDto;
import com.marketcollection.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
@Transactional
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    // 카테고리 루트 생성
    public ItemCategoryDto createCategoryRoot() {
        List<ItemCategory> categories = categoryRepository.findAll();
        Map<Long, List<ItemCategoryDto>> subCategoriesMap = categories.stream()
                .map(c -> new ItemCategoryDto(c.getId(), c.getCategoryName(), c.getParentId()))
                .collect(groupingBy(ItemCategoryDto::getParentId));

        ItemCategoryDto rootItemCategoryDto = new ItemCategoryDto(0L, "ROOT", null);
        addSubCategories(rootItemCategoryDto, subCategoriesMap);

        return rootItemCategoryDto;
    }

    // 하위 카테고리 생성
    private void addSubCategories(ItemCategoryDto parentItemCategoryDto, Map<Long, List<ItemCategoryDto>> subCategoriesMap) {
        List<ItemCategoryDto> subCategories = subCategoriesMap.get(parentItemCategoryDto.getId());
        if(subCategories == null) return;

        parentItemCategoryDto.setSubCategories(subCategories);
        subCategories.stream().forEach(s -> { addSubCategories(s, subCategoriesMap); });
    }
}
