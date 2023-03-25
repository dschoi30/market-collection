package com.marketcollection.domain.category.service;

import com.marketcollection.domain.category.ItemCategory;
import com.marketcollection.domain.category.dto.ItemCategoryDto;
import com.marketcollection.domain.category.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/*@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CategoryServiceTest {

    @InjectMocks CategoryService categoryService;
    @Mock CategoryRepository categoryRepository;

    private List<Category> createCategory() {
        Category sub1 = Category.builder().categoryName("sub1").parentId(0L).build();
        Category sub2 = Category.builder().categoryName("sub2").parentId(0L).build();
        Category sub11 = Category.builder().categoryName("sub1-1").parentId(1L).build();
        Category sub12 = Category.builder().categoryName("sub1-2").parentId(1L).build();
        Category sub21 = Category.builder().categoryName("sub2-1").parentId(2L).build();

        Category s1 = categoryRepository.save(sub1);
        Category s2 = categoryRepository.save(sub2);
        Category s11 = categoryRepository.save(sub11);
        Category s12 = categoryRepository.save(sub12);
        Category s21 = categoryRepository.save(sub21);
        return List.of(s1, s2, s11, s12, s21);
    }

    @Rollback(value = false)
    @Test
    public void saveCategory() {
        //given
        List<Category> categories = createCategory();
        //when
        CategoryDto rootCategory = categoryService.createRootCategory();

        //then
        Assertions.assertThat(rootCategory.getSubCategories().get(0).getCategoryName()).isEqualTo("sub1");
        Assertions.assertThat(rootCategory.getSubCategories().get(1).getSubCategories().get(0).getCategoryName()).isEqualTo("sub2-1");
    }
}*/
@ExtendWith(MockitoExtension.class)
class ItemCategoryServiceTest {
    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    public void 최상위_카테고리_생성() throws Exception {
        //given
        List<ItemCategory> itemCategoryEntities = createCategoryEntities();
        given(categoryRepository.findAll())
                .willReturn(itemCategoryEntities);

        //when
        ItemCategoryDto categoryRoot = categoryService.createRootCategory();

        //then
        verify(categoryRepository, atLeastOnce()).findAll();
        // root
        assertThat(categoryRoot.getSubCategories().size(), is(2));
        // sub1
        assertThat(categoryRoot.getSubCategories().get(0).getSubCategories().size(), is(2));
        // sub2
        assertThat(categoryRoot.getSubCategories().get(1).getSubCategories().size(), is(2));
        System.out.println("name="+categoryRoot.getSubCategories().get(1).getSubCategories().get(0).getCategoryName());

    }

    private List<ItemCategory> createCategoryEntities() {
        ItemCategory sub1 = new ItemCategory("SUB1", 0l);
        ItemCategory sub2 = new ItemCategory("SUB2", 0l);
        ItemCategory sub11 = new ItemCategory("SUB1-1", 1l);
        ItemCategory sub12 = new ItemCategory("SUB1-2", 1l);
        ItemCategory sub21 = new ItemCategory("SUB2-1", 2l);
        ItemCategory sub22 = new ItemCategory("SUB2-2", 2l);
        ReflectionTestUtils.setField(sub1, "id", 1l);
        ReflectionTestUtils.setField(sub2, "id", 2l);
        ReflectionTestUtils.setField(sub11, "id", 3l);
        ReflectionTestUtils.setField(sub12, "id", 4l);
        ReflectionTestUtils.setField(sub21, "id", 5l);
        ReflectionTestUtils.setField(sub22, "id", 6l);

        List<ItemCategory> itemCategoryEntities = List.of(
                sub1, sub2, sub11, sub12, sub21, sub22
        );
        return itemCategoryEntities;
    }
}