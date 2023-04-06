package com.marketcollection.domain.item.controller;

import com.marketcollection.domain.category.dto.ItemCategoryDto;
import com.marketcollection.domain.category.service.CategoryService;
import com.marketcollection.domain.common.LoginMemberInfo;
import com.marketcollection.domain.common.PageCursor;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.dto.*;
import com.marketcollection.domain.item.service.ItemService;
import com.marketcollection.domain.order.dto.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class ItemController extends LoginMemberInfo {

    private final ItemService itemService;
    private final CategoryService categoryService;

    // 상품 등록 페이지
    @GetMapping("/admin/item/new")
    public String saveItem(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());

        return "item/itemForm";
    }

    // 상품 상세 조회
    @GetMapping("/items/{itemId}")
    public String getItemDetail(Model model, @PathVariable Long itemId,
                                HttpServletRequest request, HttpServletResponse response) {
        try {
            ItemDetailDto itemDetailDto = itemService.getItemDetail(itemId, request, response);
            model.addAttribute("item", itemDetailDto);
            model.addAttribute("orderRequestDto", new OrderRequestDto());
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "상품이 존재하지 않습니다.");
            return "redirect:/";
        }

        return "item/itemDetail";
    }

    // 관리자 상품 관리
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String getAdminItemPage(Model model, ItemSearchDto itemSearchDto,
                                   @PathVariable Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 20);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 10);

        return "item/adminItem";
    }

    // 관리자 상품 수정
    @GetMapping("/admin/item/{itemId}")
    public String findById(Model model, @PathVariable Long itemId) {
        try {
            ItemFormDto itemFormDto = itemService.findById(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "상품이 존재하지 않습니다.");
        }
        return "item/itemForm";
    }

    // 카테고리별 상품 목록 조회
    @GetMapping("/categories/{categoryId}")
    public String getCategoryItemList(Model model, ItemSearchDto itemSearchDto,
                                        @PathVariable Long categoryId, Optional<Integer> page,
                                        HttpServletRequest request) {
        // 카테고리
        ItemCategoryDto itemCategoryDto = categoryService.createCategoryRoot();
        String categoryName = itemCategoryDto.findCategoryName(categoryId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("itemCategoryDto", itemCategoryDto);

        // 상품 목록
        itemSearchDto.setCategoryId(categoryId);
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 100);
        Page<ItemListDto> items = itemService.getItemListPage(itemSearchDto, pageable);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("items", items);
        model.addAttribute("maxPage", 10);

        // 최근 본 상품
        List<ItemListDto> recentItems = itemService.getRecentViewList(request);
        model.addAttribute("recentItems", recentItems);

        return "/item/categoryItemList";
    }
}
