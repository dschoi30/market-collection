package com.marketcollection.domain.order.controller;

import com.marketcollection.common.auth.LoginUser;
import com.marketcollection.common.auth.dto.SessionUser;
import com.marketcollection.common.exception.ErrorCode;
import com.marketcollection.domain.category.dto.ItemCategoryDto;
import com.marketcollection.domain.category.service.CategoryService;
import com.marketcollection.domain.common.HeaderInfo;
import com.marketcollection.domain.common.PageCursor;
import com.marketcollection.domain.order.dto.*;
import com.marketcollection.domain.order.exception.InvalidPaymentAmountException;
import com.marketcollection.domain.order.service.OrderService;
import com.marketcollection.domain.order.dto.PaymentResponse;
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
public class OrderController extends HeaderInfo {

    private final CategoryService categoryService;
    private final OrderService orderService;

    private static final int PAGE_SIZE = 10;
    private static final int MAX_PAGE = 10;

    @ModelAttribute
    public void initItemCategory(Model model) {
        ItemCategoryDto itemCategoryDto = categoryService.createCategoryRoot();
        model.addAttribute("itemCategoryDto", itemCategoryDto);
    }

    // 장바구니 미경유 주문 정보 생성
    @PostMapping("/order/direct")
    public String setDirectOrderInfo(Model model, @LoginUser SessionUser user, OrderRequest orderRequest) {
        List<OrderItemRequest> orderItemRequests = orderRequest.getOrderItemRequests();
        for (OrderItemRequest orderItemRequest : orderItemRequests) {
            if(orderItemRequest.getCount() <= 0) throw new IllegalArgumentException("최소 1개 이상 주문해야 합니다.");
        }
        OrderDto orderDto = orderService.setOrderInfo(user.getEmail(), orderRequest, "Y");
        model.addAttribute("orderDto", orderDto);

        return "order/order";
    }

    // 장바구니 경유 주문 정보 생성
    @PostMapping("/order")
    public String setOrderInfo(Model model, @LoginUser SessionUser user, OrderRequest orderRequest) {
        List<OrderItemRequest> orderItemRequests = orderRequest.getOrderItemRequests();
        for (OrderItemRequest orderItemRequest : orderItemRequests) {
            if(orderItemRequest.getCount() <= 0) throw new IllegalArgumentException("최소 1개 이상 주문해야 합니다.");
        }
        OrderDto orderDto = orderService.setOrderInfo(user.getEmail(), orderRequest, "N");
        model.addAttribute("orderDto", orderDto);

        return "order/order";
    }

    // 주문 처리
    @PostMapping("/order/checkout")
    public @ResponseBody ResponseEntity order(@LoginUser SessionUser user, @Valid @RequestBody OrderDto orderDto) {
        OrderResponse orderResponse = orderService.order(user.getEmail(), orderDto);

        return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.OK);
    }

    // 결제 처리
    @GetMapping("/order/checkout/success")
    public String handlePayment(Model model, @LoginUser SessionUser user, String paymentKey,
                                String orderId, Long amount) {
        // 결제 금액 검증(결제 요청 시 금액을 변조하여 실 결제 금액이 주문 금액과 달라지는 경우를 막기 위함)
        if(!orderService.validatePaymentAmount(orderId, amount)) {
            throw new InvalidPaymentAmountException(ErrorCode.INVALID_PAYMENT_AMOUNT);
        };

        try {
            PaymentResponse paymentResponse = orderService.handlePayment(paymentKey, orderId, amount);
            if(paymentResponse.getTotalAmount() != amount) {
                orderService.abortPayment(orderId);
                throw new InvalidPaymentAmountException(ErrorCode.INVALID_PAYMENT_AMOUNT);
            }
            model.addAttribute("payment", paymentResponse);
            model.addAttribute("user", user);
        } catch (Exception e) {
            e.printStackTrace();
            orderService.abortOrder(orderId);
            model.addAttribute("errorMessage", "결제 요청에 실패했습니다. 다시 시도해 주세요.");

            return "redirect:/";
        }

        return "payment/paymentSuccess";
    }

    // 결제 실패
    @GetMapping("/order/checkout/fail")
    public String fail() {
        return "payment/paymentFail";
    }

    // 내 주문 내역 조회
    @GetMapping("/orders")
    public String initOrderHistory(Model model, @LoginUser SessionUser user) {
        model.addAttribute("user", user);

        return "order/orderHistory";
    }

    // 내 주문 내역 페이징
    @GetMapping({"/orders/0", "/orders/{lastOrderId}"})
    public @ResponseBody PageCursor<OrderHistoryDto> getOrderHistory(@LoginUser SessionUser user,
                                                                     @PathVariable(required = false) Long lastOrderId,
                                                                     @RequestParam String searchDateType) {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);

        return orderService.getOrderHistory(
                user.getEmail(), searchDateType, lastOrderId, pageable);
    }

    // 내 주문 내역 상세 조회
    @GetMapping("/orders/{orderId}/detail")
    public OrderDetailResponse getOrderDetail(Model model, @LoginUser SessionUser user,
                                                            @PathVariable Long orderId) {
        OrderDetailResponse orderDetailResponse = orderService.getOrderHistoryDetail(user.getEmail(), orderId);

        model.addAttribute("orderDetailDto", orderDetailResponse);

        return orderDetailResponse;
    }

    // 관리자 주문 관리
    @GetMapping({"/admin/orders", "/admin/orders/{page}"})
    public String getAdminOrderList(Model model, OrderSearchDto orderSearchDto,
                                    @PathVariable("page") Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, PAGE_SIZE);
        Page<AdminOrderDto> orders = orderService.getAdminOrderList(orderSearchDto, pageable);

        model.addAttribute("orders", orders);
        model.addAttribute("orderSearchDto", orderSearchDto);
        model.addAttribute("maxPage", MAX_PAGE);

        return "order/adminOrderList";
    }

    // 주문 취소
    @PostMapping("/orders/{orderNumber}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@LoginUser SessionUser user,
                                                    @PathVariable("orderNumber") String orderNumber,
                                                    @RequestBody OrderCancelDto orderCancelDto) {
        if(!orderService.validateOrder(orderNumber, user.getEmail())) {
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.UNAUTHORIZED);
        }
        try {
            orderService.cancelOrder(orderCancelDto);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("주문 취소에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(orderNumber, HttpStatus.OK);
    }

    // 주문 실패 처리
    @PostMapping("/order/{orderNumber}/fail")
    public @ResponseBody ResponseEntity failOrder(@LoginUser SessionUser user,
                                                  @PathVariable("orderNumber") String orderNumber) {
        if(!orderService.validateOrder(orderNumber, user.getEmail())) {
            return new ResponseEntity<String>("주문 실패 처리 권한이 없습니다.", HttpStatus.UNAUTHORIZED);
        }
        orderService.abortOrder(orderNumber);

        return new ResponseEntity<String>(orderNumber, HttpStatus.OK);
    }
}
