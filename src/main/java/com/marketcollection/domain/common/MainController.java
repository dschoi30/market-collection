package com.marketcollection.domain.common;

import com.marketcollection.domain.category.dto.ItemCategoryDto;
import com.marketcollection.domain.category.service.CategoryService;
import com.marketcollection.domain.discount.dto.DailySaleItemListDto;
import com.marketcollection.domain.discount.repository.DiscountRepository;
import com.marketcollection.domain.item.dto.ItemListDto;
import com.marketcollection.domain.item.dto.ItemSearchDto;
import com.marketcollection.domain.item.repository.ItemRepository;
import com.marketcollection.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class MainController extends LoginMemberInfo {

    private final CategoryService categoryService;
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final DiscountRepository discountRepository;

    @GetMapping("/main")
    public String mainPage(Model model, HttpServletRequest request) {
        // 카테고리
        ItemCategoryDto itemCategoryDto = categoryService.createCategoryRoot();
        model.addAttribute("itemCategoryDto", itemCategoryDto);

        // 상품 목록
        List<ItemListDto> weeklyHotItems = itemRepository.getWeeklyHotItems();
        model.addAttribute("hotItems", weeklyHotItems);
        List<ItemListDto> discountItems = itemRepository.getMonthlyHighestDiscountRateItems();
        model.addAttribute("discountItems", discountItems);
        List<DailySaleItemListDto> dailySaleItems = discountRepository.getDailySaleItems();
        model.addAttribute("dailySaleItems", dailySaleItems);
        // 최근 본 상품
        List<ItemListDto> recentItems = itemService.getRecentViewList(request);
        model.addAttribute("recentItems", recentItems);

        return "main";
    }
}