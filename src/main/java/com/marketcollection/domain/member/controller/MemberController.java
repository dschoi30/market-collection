package com.marketcollection.domain.member.controller;

import com.marketcollection.domain.category.dto.ItemCategoryDto;
import com.marketcollection.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/member")
@Controller
public class MemberController {

    private final CategoryService categoryService;

    @ModelAttribute
    public void initItemCategory(Model model) {
        ItemCategoryDto itemCategoryDto = categoryService.createCategoryRoot();
        model.addAttribute("itemCategoryDto", itemCategoryDto);
    }

    @GetMapping("/login")
    public String login() {
        return "member/login";
    }
}
