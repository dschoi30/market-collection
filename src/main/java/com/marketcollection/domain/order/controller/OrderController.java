package com.marketcollection.domain.order.controller;

import com.marketcollection.common.auth.LoginUser;
import com.marketcollection.common.auth.dto.SessionUser;
import com.marketcollection.domain.order.dto.OrderDto;
import com.marketcollection.domain.order.dto.OrderRequestDto;
import com.marketcollection.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/order/direct")
    public String setDirectOrderInfo(Model model, @LoginUser SessionUser user, @ModelAttribute OrderRequestDto orderRequestDto) {

        OrderDto orderDto = orderService.setOrderInfo(user.getEmail(), orderRequestDto);

        model.addAttribute("orderDto", orderDto);

        return "order/order";
    }

    @PostMapping("/order/checkout")
    public String order(@LoginUser SessionUser user, @ModelAttribute OrderDto orderDto) {
        System.out.println("itemName = " + orderDto.getOrderItemDtos().get(0).getItemName());
        System.out.println("orderDto = " + orderDto.toString());
        Long orderId = orderService.order(user.getEmail(), orderDto);

        return "redirect:/";
    }
}
