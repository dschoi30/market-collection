package com.marketcollection.domain.common;

import com.marketcollection.common.auth.LoginUser;
import com.marketcollection.common.auth.dto.SessionUser;
import com.marketcollection.domain.category.dto.ItemCategoryDto;
import com.marketcollection.domain.category.service.CategoryService;
import com.marketcollection.domain.item.dto.ItemListDto;
import com.marketcollection.domain.item.dto.ItemSearchDto;
import com.marketcollection.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final ItemService itemService;
    private final CategoryService categoryService;

    @GetMapping("/")
    public String mainPage(Model model, @LoginUser SessionUser user, ItemSearchDto itemSearchDto,
                           Optional<Integer> page, HttpServletRequest request) {
        // 메뉴 상단 회원 정보
        if(user != null) {
            model.addAttribute("userName", user.getUserName());
            model.addAttribute("grade", user.getGrade().getTitle());
        }

        // 카테고리
        ItemCategoryDto itemCategoryDto = categoryService.createCategoryRoot();
        model.addAttribute("itemCategoryDto", itemCategoryDto);

        // 상품 목록
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 8);
        Page<ItemListDto> items = itemService.getItemListPage(itemSearchDto, pageable);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("items", items);
        model.addAttribute("maxPage", 10);

        // 최근 본 상품
        List<ItemListDto> recentItems = itemService.getRecentViewList(request);
        model.addAttribute("recentItems", recentItems);

        return "main";
    }
}