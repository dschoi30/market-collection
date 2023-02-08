package com.marketcollection.domain.item.controller;

import com.marketcollection.domain.item.dto.ItemDetailDto;
import com.marketcollection.domain.item.dto.ItemFormDto;
import com.marketcollection.common.auth.dto.SessionUser;
import com.marketcollection.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class ItemController {

    private final HttpSession httpSession;
    private final ItemService itemService;

    @GetMapping("/admin/item/new")
    public String saveItem(Model model) {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user != null) {
            model.addAttribute("userName", user.getUserName());
        }
        model.addAttribute("itemFormDto", new ItemFormDto());

        return "item/itemForm";
    }

    @GetMapping("/items/{itemId}")
    public String getItemDetail(Model model, @PathVariable("itemId") Long itemId) {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user != null) {
            model.addAttribute("userName", user.getUserName());
        }

        try {
            ItemDetailDto itemDetailDto = itemService.getItemDetail(itemId);
            model.addAttribute("item", itemDetailDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "상품이 존재하지 않습니다.");
            return "redirect:/";
        }

        return "item/itemDetail";
    }
}
