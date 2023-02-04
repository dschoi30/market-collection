package com.marketcollection.domain.item.itemController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/items")
@Controller
public class ItemController {

    @GetMapping("/new")
    public String saveItem() {
        return "item/itemForm";
    }
}
