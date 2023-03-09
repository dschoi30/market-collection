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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class OrderController {

    private final OrderService orderService;

    // 헤더에 회원 정보 출력
    @ModelAttribute
    public void setMemberInfo(Model model, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("userName", user.getUserName());
        }
    }

    // 장바구니 미경유 주문 정보 생성
    @PostMapping("/order/direct")
    public String setDirectOrderInfo(Model model, @LoginUser SessionUser user, @Valid OrderRequestDto orderRequestDto) {
        List<OrderItemRequestDto> orderItemRequestDtos = orderRequestDto.getOrderItemRequestDtos();
        for (OrderItemRequestDto orderItemRequestDto : orderItemRequestDtos) {
            if(orderItemRequestDto.getCount() <= 0) throw new IllegalArgumentException("최소 1개 이상 주문해야 합니다.");
        }
        OrderDto orderDto = orderService.setOrderInfo(user.getEmail(), orderRequestDto, "Y");
        model.addAttribute("orderDto", orderDto);

        return "order/order";
    }

    // 장바구니 경유 주문 정보 생성
    @PostMapping("/order")
    public String setOrderInfo(Model model, @LoginUser SessionUser user, @Valid OrderRequestDto orderRequestDto) {
        List<OrderItemRequestDto> orderItemRequestDtos = orderRequestDto.getOrderItemRequestDtos();
        for (OrderItemRequestDto orderItemRequestDto : orderItemRequestDtos) {
            if(orderItemRequestDto.getCount() <= 0) throw new IllegalArgumentException("최소 1개 이상 주문해야 합니다.");
        }
        OrderDto orderDto = orderService.setOrderInfo(user.getEmail(), orderRequestDto, "N");
        model.addAttribute("orderDto", orderDto);

        return "order/order";
    }

    // 주문 처리
    @PostMapping("/order/checkout")
    public @ResponseBody ResponseEntity order(@LoginUser SessionUser user, @Valid @RequestBody OrderDto orderDto) {
        Long orderId = orderService.order(user.getEmail(), orderDto);

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    // 내 주문 내역 조회
    @GetMapping({"/orders", "/orders/{page}"})
    public String getOrderHistory(Model model, @LoginUser SessionUser user,
                                  OrderSearchDto orderSearchDto, @PathVariable("page") Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<OrderHistoryDto> orders = orderService.getOrderHistory(user.getEmail(), orderSearchDto, pageable);

        model.addAttribute("orders", orders);
        model.addAttribute("orderSearchDto", orderSearchDto);
        model.addAttribute("maxPage", 10);

        return "order/orderHistory";
    }

    // 관리자 주문 관리
    @GetMapping({"/admin/orders", "/admin/orders/{page}"})
    public String getAdminOrderList(Model model,
                                    OrderSearchDto orderSearchDto, @PathVariable("page") Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<AdminOrderDto> orders = orderService.getAdminOrderList(orderSearchDto, pageable);

        model.addAttribute("orders", orders);
        model.addAttribute("orderSearchDto", orderSearchDto);
        model.addAttribute("maxPage", 10);

        return "order/adminOrderList";
    }

    // 주문 취소
    @PostMapping("/orders/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@LoginUser SessionUser user, @PathVariable("orderId") Long orderId) {
        if(!orderService.validateOrder(orderId, user.getEmail())) {
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.UNAUTHORIZED);
        }
        orderService.cancelOrder(orderId);

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
