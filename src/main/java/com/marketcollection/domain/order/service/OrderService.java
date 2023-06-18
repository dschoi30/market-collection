package com.marketcollection.domain.order.service;

import com.marketcollection.domain.cart.service.CartService;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.repository.ItemRepository;
import com.marketcollection.domain.member.Member;
import com.marketcollection.domain.member.repository.MemberRepository;
import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.OrderItem;
import com.marketcollection.domain.order.dto.*;
import com.marketcollection.domain.order.repository.OrderItemRepository;
import com.marketcollection.domain.order.repository.OrderRepository;
import com.marketcollection.domain.order.dto.PaymentSuccessDto;
import com.marketcollection.domain.order.dto.PGResponseDto;
import com.marketcollection.domain.point.service.PointService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentContext paymentContext;
    private final CartService cartService;
    private final PointService pointService;

    @Value("${tossSecretKey}")
    private String secretKey;
    private String tossUrl = "https://api.tosspayments.com/v1/payments/";

    // 주문 정보 생성
    @Transactional(readOnly = true)
    public OrderDto setOrderInfo(String memberId, OrderRequestDto orderRequestDto, String directOrderYn) {
        OrderDto orderDto = new OrderDto();

        // 주문자 정보
        Member member = memberRepository.findByEmail(memberId).orElseThrow(EntityNotFoundException::new);
        orderDto.setMemberInfo(member);

        // 주문 상품 정보
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        List<OrderItemRequestDto> orderItemRequestDtos = orderRequestDto.getOrderItemRequestDtos();
        for (OrderItemRequestDto orderItemRequestDto : orderItemRequestDtos) {
            Item item = itemRepository.findById(orderItemRequestDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderItemDto orderItemDto = new OrderItemDto(item, orderItemRequestDto.getCount(),
                    member.getGrade().getPointSavingRate());
            orderItemDtos.add(orderItemDto);
        }

        orderDto.setOrderItemInfo(orderItemDtos);
        orderDto.setDirectOrderYn(directOrderYn);

        return orderDto;
    }

    // 주문 처리
    @Transactional
    public OrderResponseDto order(String memberId, OrderDto orderDto) {
        // 주문자 정보로 회원 정보 업데이트
        Member member = memberRepository.findByEmail(memberId).orElseThrow(EntityNotFoundException::new);
        member.updateOrderInfo(orderDto);

        // 주문 등록
        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderItemDto> orderItemDtos = orderDto.getOrderItemDtos();
        for (OrderItemDto orderItemDto : orderItemDtos) {
            Item item = itemRepository.findWithPessimisticLockById(orderItemDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item, orderItemDto.getCount(),
                                                            member.getGrade().getPointSavingRate());
            orderItems.add(orderItem);
        }

        Order order = Order.createOrder(member, orderItems, orderDto);
        orderItems.forEach(oi -> oi.setOrder(order));
        orderRepository.save(order);

        // 포인트 입출 내역 등록
        orderItems.forEach(oi -> pointService.createOrderPoint(member, oi));

        if (orderDto.getUsingPoint() > 0) {
            pointService.createUsingPoint(member, orderDto);
        }

        // 장바구니 목록 정리
        if(Objects.equals(orderDto.getDirectOrderYn(), "N")) {
            cartService.deleteCartItemsAfterOrder(member.getId(), orderItems);
        }

        return order.toDto();
    }

    // 결제 처리
    @Transactional
    public PaymentSuccessDto handlePayment(String paymentKey, String orderId, Long amount) {
        // 결제 승인 요청
        RestTemplate restTemplate = new RestTemplate();

        URI uri = URI.create(tossUrl + "confirm");
        JSONObject params = createPaymentParams(orderId, amount, paymentKey);
        HttpHeaders headers = createHeaders();

        PGResponseDto pgResponseDto = restTemplate
                .postForEntity(uri, new HttpEntity<>(params, headers), PGResponseDto.class)
                .getBody();

        Assert.notNull(pgResponseDto, "결제 승인 요청에 실패했습니다.");

        // 결제 정보 저장
        Order order = orderRepository.findByOrderNumber(orderId).orElseThrow(EntityNotFoundException::new);
        order.savePaymentInfo(pgResponseDto);

        // 결제 수단별 결제 정보 저장
        PaymentService paymentService = paymentContext.getPaymentService(pgResponseDto);
        paymentService.savePaymentInfo(pgResponseDto, order);

        return PaymentSuccessDto.of(pgResponseDto);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();

        String encodedAuth = encodeSecretKey(secretKey);
        headers.setBasicAuth(encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return headers;
    }

    private String encodeSecretKey(String secretKey) {
        secretKey = secretKey + ":";
        return new String(Base64.getEncoder().encode(secretKey.getBytes(StandardCharsets.UTF_8)));
    }

    private JSONObject createPaymentParams(String orderId, Long amount, String paymentKey) {
        JSONObject params = new JSONObject();
        params.put("orderId", orderId);
        params.put("amount", amount);
        params.put("paymentKey", paymentKey);
        return params;
    }

    private JSONObject createPaymentCancelParams(String cancelReason, int cancelAmount) {
        JSONObject params = new JSONObject();
        params.put("cancelReason", cancelReason);
        params.put("cancelAmount", cancelAmount);
        return params;
    }

    // 결제 금액 유효성 검사
    @Transactional
    public boolean validatePaymentAmount(String orderId, Long amount) {
        Order order = orderRepository.findByOrderNumber(orderId).orElseThrow(EntityNotFoundException::new);
        boolean isValidAmount = order.getTotalPaymentAmount() == amount;
        if(!isValidAmount) {
            order.failOrder();
        }

        return isValidAmount;
    }

    // 주문 실패 처리
    @Transactional
    public void abortOrder(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber).orElseThrow(EntityNotFoundException::new);
        order.failOrder();
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(PaymentCancelDto paymentCancelDto) {
        Order order = orderRepository.findById(paymentCancelDto.getOrderId()).orElseThrow(EntityNotFoundException::new);
        String paymentKey = order.getPaymentKey();
        String cancelReason = paymentCancelDto.getCancelReason();

        List<Long> orderItemIds = paymentCancelDto.getOrderItemIds();
        List<OrderItem> orderItems = orderItemRepository.findByIdIn(orderItemIds);
        int cancelAmount = orderItems.stream()
                .mapToInt(OrderItem::getOrderPrice)
                .sum();

        requestPaymentCancel(paymentKey, cancelReason, cancelAmount);

        order.cancelOrder();
    }

    public void requestPaymentCancel(String paymentKey, String cancelReason, int cancelAmount) {
        RestTemplate restTemplate = new RestTemplate();

        URI uri = URI.create(tossUrl + paymentKey + "/cancel");

        JSONObject params = createPaymentCancelParams(cancelReason, cancelAmount);
        HttpHeaders headers = createHeaders();

        PGResponseDto pgResponseDto = restTemplate
                .postForEntity(uri, new HttpEntity<>(params, headers), PGResponseDto.class)
                .getBody();

        Assert.notNull(pgResponseDto, "결제 취소 요청에 실패했습니다.");
        System.out.println(pgResponseDto.toString());
    }

    // 주문자 유효성 검사
    @Transactional(readOnly = true)
    public boolean validateOrder(String orderNumber, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        Order order = orderRepository.findByOrderNumber(orderNumber).orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        return StringUtils.equals(member.getEmail(), savedMember.getEmail());
    }

    // 내 주문 정보 조회
    @Transactional(readOnly = true)
    public Page<OrderHistoryDto> getOrderHistory(String memberId, OrderSearchDto orderSearchDto, Pageable pageable) {
        Member member = memberRepository.findByEmail(memberId).orElseThrow(EntityNotFoundException::new);
        List<Order> orders = orderRepository.findOrders(member.getId(), orderSearchDto, pageable);
        Long total = orderRepository.countOrders(member.getId(), orderSearchDto);

        List<OrderHistoryDto> orderHistoryDtos = new ArrayList<>();
        for (Order order : orders) {
            OrderHistoryDto orderHistoryDto = new OrderHistoryDto(order);
            List<OrderItemDto> orderItemDtos = new ArrayList<>();
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                OrderItemDto orderItemDto = new OrderItemDto(orderItem);
                orderItemDtos.add(orderItemDto);
            }
            orderHistoryDto.setOrderItemDtos(orderItemDtos);
            orderHistoryDtos.add(orderHistoryDto);
        }
        return new PageImpl<OrderHistoryDto>(orderHistoryDtos, pageable, total);
    }

    @Transactional(readOnly = true)
    public Page<OrderHistoryDto2> getOrderHistory2(String email, OrderSearchDto orderSearchDto, Pageable pageable) {
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);

        return orderRepository.findOrderHistory(member.getId(), orderSearchDto, pageable);
    }
    // 관리자 주문 관리
    @Transactional(readOnly = true)
    public Page<AdminOrderDto> getAdminOrderList(OrderSearchDto orderSearchDto, Pageable pageable) {
        List<Order> orders = orderRepository.findAllOrders(orderSearchDto, pageable);
        Long total = orderRepository.countAllOrders(orderSearchDto);

        List<AdminOrderDto> adminOrderDtos = new ArrayList<>();
        for (Order order : orders) {
            AdminOrderDto adminOrderDto = new AdminOrderDto();
            adminOrderDto.addOrderInfo(order);
            Member member = memberRepository.findById(order.getMember().getId()).orElseThrow(EntityNotFoundException::new);
            adminOrderDto.addMemberInfo(member.getEmail());
            List<OrderItem> orderItems = order.getOrderItems();
            adminOrderDto.addItemInfo(orderItems.get(0).getItem().getRepImageUrl(), // 주문 목록 중 첫 번째 상품의 정보를 대표로 뷰에 노출
                    orderItems.get(0).getItem().getItemName(), orderItems.size(), order.getTotalOrderPrice());
            adminOrderDtos.add(adminOrderDto);
        }

        return new PageImpl<AdminOrderDto>(adminOrderDtos, pageable, total);
    }

}
