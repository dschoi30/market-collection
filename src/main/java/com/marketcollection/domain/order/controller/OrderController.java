package com.marketcollection.domain.order.controller;

import com.marketcollection.common.auth.LoginUser;
import com.marketcollection.common.auth.dto.SessionUser;
import com.marketcollection.domain.order.dto.OrderDto;
import com.marketcollection.domain.order.dto.OrderHistoryDto;
import com.marketcollection.domain.order.dto.OrderRequestDto;
import com.marketcollection.domain.order.dto.OrderSearchDto;
import com.marketcollection.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class OrderController {

    private final OrderService orderService;

    @ModelAttribute
    public void setMemberInfo(Model model, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("userName", user.getUserName());
        }
    }

    @PostMapping("/order")
    public String setDirectOrderInfo(Model model, @LoginUser SessionUser user, @ModelAttribute OrderRequestDto orderRequestDto) {

        OrderDto orderDto = orderService.setOrderInfo(user.getEmail(), orderRequestDto);
        model.addAttribute("orderDto", orderDto);

        return "order/order";
    }

    @PostMapping("/order/checkout")
    public String order(@LoginUser SessionUser user, @ModelAttribute OrderDto orderDto) {

        Long orderId = orderService.order(user.getEmail(), orderDto);

        return "redirect:/";
    }

    @GetMapping({"/orders", "/orders/{page}"})
    public String getOrderHistory(Model model, @LoginUser SessionUser user,
                                  OrderSearchDto orderSearchDto, @PathVariable("page") Optional<Integer> page) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 20);
        Page<OrderHistoryDto> orders = orderService.getOrderHistory(user.getEmail(), orderSearchDto, pageable);

        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);
        model.addAttribute("orders", orders);
        model.addAttribute("orderSearchDto", orderSearchDto);

        return "order/orderHistory";
    }
}
