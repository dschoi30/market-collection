package com.marketcollection.domain.order.controller;

import com.marketcollection.common.auth.LoginUser;
import com.marketcollection.common.auth.dto.SessionUser;
import com.marketcollection.domain.order.dto.*;
import com.marketcollection.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/order/direct")
    public String setDirectOrderInfo(Model model, @LoginUser SessionUser user, @ModelAttribute OrderRequestDto orderRequestDto) {

        OrderDto orderDto = orderService.setOrderInfo(user.getEmail(), orderRequestDto, "Y");
        model.addAttribute("orderDto", orderDto);

        return "order/order";
    }
    @PostMapping("/order")
    public String setOrderInfo(Model model, @LoginUser SessionUser user, @ModelAttribute OrderRequestDto orderRequestDto) {

        OrderDto orderDto = orderService.setOrderInfo(user.getEmail(), orderRequestDto, "N");
        model.addAttribute("orderDto", orderDto);

        return "order/order";
    }

    @PostMapping("/order/checkout")
    public String order(@LoginUser SessionUser user, @ModelAttribute OrderDto orderDto) {
        System.out.println("direct===" + orderDto.getDirectOrderYn());
        Long orderId = orderService.order(user.getEmail(), orderDto);

        return "redirect:/";
    }

    @GetMapping({"/orders", "/orders/{page}"})
    public String getOrderHistory(Model model, @LoginUser SessionUser user,
                                  OrderSearchDto orderSearchDto, @PathVariable("page") Optional<Integer> page) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<OrderHistoryDto> orders = orderService.getOrderHistory(user.getEmail(), orderSearchDto, pageable);

        model.addAttribute("orders", orders);
        model.addAttribute("orderSearchDto", orderSearchDto);
        model.addAttribute("maxPage", 5);

        return "order/orderHistory";
    }

    @GetMapping({"/admin/orders", "/admin/orders/{page}"})
    public String getAdminOrderList(Model model,
                                    OrderSearchDto orderSearchDto, @PathVariable("page") Optional<Integer> page) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<AdminOrderDto> orders = orderService.getAdminOrderList(orderSearchDto, pageable);

        model.addAttribute("orders", orders);
        model.addAttribute("orderSearchDto", orderSearchDto);
        model.addAttribute("maxPage", 5);

        return "order/adminOrderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@LoginUser SessionUser user, @PathVariable("orderId") Long orderId) {
        if(!orderService.validateOrder(orderId, user.getEmail())) {
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.UNAUTHORIZED);
        }
        orderService.cancelOrder(orderId);

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
