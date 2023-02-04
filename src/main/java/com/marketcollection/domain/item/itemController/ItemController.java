package com.marketcollection.domain.item.itemController;

import com.marketcollection.api.item.dto.ItemFormDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ItemController {

    @GetMapping("/admin/item/new")
    public String saveItem(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }
}
