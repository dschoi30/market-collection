package com.marketcollection.common.main;

import com.marketcollection.common.auth.LoginUser;
import com.marketcollection.domain.item.dto.ItemListDto;
import com.marketcollection.domain.item.dto.ItemSearchDto;
import com.marketcollection.common.auth.dto.SessionUser;
import com.marketcollection.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final ItemService itemService;

    @GetMapping("/")
    public String mainPage(Model model, @LoginUser SessionUser user, ItemSearchDto itemSearchDto,
                           Optional<Integer> page, HttpServletRequest request) {
        if(user != null) {
            model.addAttribute("userName", user.getUserName());
        }

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 8);
        Page<ItemListDto> items = itemService.getItemListPage(itemSearchDto, pageable);
        List<ItemListDto> recentItems = itemService.getRecentViewList(request);
        model.addAttribute("items", items);
        model.addAttribute("recentItems", recentItems);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 10);

        return "main";
    }
}
