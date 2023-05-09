package com.marketcollection.domain.discount.controller;

import com.marketcollection.domain.discount.dto.DiscountRequestDto;
import com.marketcollection.domain.discount.dto.DiscountResponseDto;
import com.marketcollection.domain.discount.repository.DiscountRepository;
import com.marketcollection.domain.discount.service.DiscountService;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class DiscountController {

    private final ItemService itemService;
    private final DiscountService discountService;

    private static final int PAGE_SIZE = 20;
    private static final int MAX_PAGE = 10;
    private final DiscountRepository discountRepository;

    @GetMapping("/admin/item-discount/new")
    public String saveItemDiscount(Model model, @RequestParam(value = "id") List<Long> itemIds) {
        List<Item> items = itemService.getItemList(itemIds);
        model.addAttribute("items", items);
        model.addAttribute("discountRequestDto", new DiscountRequestDto(itemIds));

        return "/discount/itemDiscountForm";
    }

    @PostMapping("/admin/item-discount")
    public @ResponseBody ResponseEntity saveItemDiscountItem(@RequestBody @Valid DiscountRequestDto dto) {
        Long itemDiscountId;
        try {
            itemDiscountId = discountService.saveDiscount(dto);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(itemDiscountId, HttpStatus.OK);
    }

    @GetMapping(value = {"/admin/item-discount/list", "/admin/item-discount/list/{page}"})
    public String getItemDiscountList(Model model, @PathVariable Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, PAGE_SIZE);
        Page<DiscountResponseDto> itemDiscountList = discountService.getItemDiscountList(pageable);

        model.addAttribute("maxPage", MAX_PAGE);
        model.addAttribute("itemDiscountList", itemDiscountList);

        return "/discount/itemDiscountList";
    }

    @GetMapping("/item-discount/finish")
    public void finishDailyItemSale(@RequestParam("itemDiscountIds[]") List<Long> itemDiscountIds) {
        try {
            discountService.finishItemDiscount(itemDiscountIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
