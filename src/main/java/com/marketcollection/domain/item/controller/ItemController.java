package com.marketcollection.domain.item.controller;

import com.marketcollection.common.auth.LoginUser;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.dto.ItemDetailDto;
import com.marketcollection.domain.item.dto.ItemFormDto;
import com.marketcollection.common.auth.dto.SessionUser;
import com.marketcollection.domain.item.dto.ItemSearchDto;
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
import javax.servlet.http.HttpSession;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class ItemController {

    private final ItemService itemService;

    @ModelAttribute
    public void setMemberInfo(Model model, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("userName", user.getUserName());
        }
    }

    @GetMapping("/admin/item/new")
    public String saveItem(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());

        return "item/itemForm";
    }

    @GetMapping("/items/{itemId}")
    public String getItemDetail(Model model, @PathVariable("itemId") Long itemId) {
        try {
            ItemDetailDto itemDetailDto = itemService.getItemDetail(itemId);
            model.addAttribute("item", itemDetailDto);
            model.addAttribute("orderRequestDto", new OrderRequestDto());
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "상품이 존재하지 않습니다.");
            return "redirect:/";
        }

        return "item/itemDetail";
    }

    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String getAdminItemPage(Model model, ItemSearchDto itemSearchDto,
                                   @PathVariable("page") Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 20);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "item/adminItem";
    }

    @GetMapping("/admin/item/{itemId}")
    public String findById(Model model, @PathVariable Long itemId) {
        try {
            ItemFormDto itemFormDto = itemService.findById(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("상품이 존재하지 않습니다.");
        }
        return "item/itemForm";
    }


}
