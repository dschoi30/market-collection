package com.marketcollection.domain.item.itemController;

import com.marketcollection.api.item.dto.ItemFormDto;
import com.marketcollection.common.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class ItemController {

    private final HttpSession httpSession;

    @GetMapping("/admin/item/new")
    public String saveItem(Model model) {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if(user != null) {
            model.addAttribute("userName", user.getUserName());
        }
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }
}
