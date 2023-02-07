package com.marketcollection.common.main;

import com.marketcollection.api.item.dto.ItemListDto;
import com.marketcollection.api.item.dto.ItemSearchDto;
import com.marketcollection.api.item.service.ApiItemService;
import com.marketcollection.common.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final HttpSession httpSession;
    private final ApiItemService apiItemService;

    @GetMapping("/")
    public String mainPage(Model model, ItemSearchDto itemSearchDto, Optional<Integer> page) {

        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user != null) {
            model.addAttribute("userName", user.getUserName());
        }

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 8);
        Page<ItemListDto> items = apiItemService.getItemListPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "main";
    }
}
