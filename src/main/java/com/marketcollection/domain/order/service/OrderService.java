package com.marketcollection.domain.order.service;

import com.marketcollection.domain.cart.service.CartService;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.repository.ItemRepository;
import com.marketcollection.domain.member.Member;
import com.marketcollection.domain.member.repository.MemberRepository;
import com.marketcollection.domain.order.Card;
import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.OrderItem;
import com.marketcollection.domain.order.dto.*;
import com.marketcollection.domain.order.repository.CardRepository;
import com.marketcollection.domain.order.repository.OrderRepository;
import com.marketcollection.domain.order.dto.PaymentResponseDto;
import com.marketcollection.domain.order.dto.TossPaymentDto;
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
@Transactional
@Service
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final CardRepository cardRepository;
    private final CartService cartService;
    private final PointService pointService;

    @Value("${tossSecretKey}")
    private String secretKey;
    private String tossUrl = "https://api.tosspayments.com/v1/payments";

    // 주문 정보 생성
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

    // 결제 승인 요청
    public PaymentResponseDto requestPaymentApproval(String paymentKey, String orderId, Long amount) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        secretKey = secretKey + ":";
        String encodedAuth = new String(Base64.getEncoder().encode(secretKey.getBytes(StandardCharsets.UTF_8)));

        headers.setBasicAuth(encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        JSONObject params = new JSONObject();
        params.put("orderId", orderId);
        params.put("amount", amount);
        params.put("paymentKey", paymentKey);

        URI uri = URI.create(tossUrl + "/confirm");

        TossPaymentDto tossPaymentDto = restTemplate.postForEntity(
                uri, new HttpEntity<>(params, headers), TossPaymentDto.class
        ).getBody();

        Assert.notNull(tossPaymentDto, "결제 승인 요청에 실패했습니다.");
        Order order = orderRepository.findByOrderNumber(orderId).orElseThrow(EntityNotFoundException::new);
        order.updatePaymnetInfo(tossPaymentDto);
        Card card = tossPaymentDto.getCard();
        cardRepository.save(card);

        return PaymentResponseDto.of(tossPaymentDto);
    }

    // 결제 금액 유효성 검사
    public boolean validatePaymentAmount(String orderId, Long amount) {
        Order order = orderRepository.findByOrderNumber(orderId).orElseThrow(EntityNotFoundException::new);
        boolean isValidAmount = order.getTotalPaymentAmount() == amount;
        if(!isValidAmount) {
            order.failOrder();
        }

        return isValidAmount;
    }

    // 주문 취소
    public void cancelOrder(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber).orElseThrow(EntityNotFoundException::new);
        requestPaymentCancel(order);
        order.cancelOrder();
    }

    // 결제 취소 요청
    public void requestPaymentCancel(Order order) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        secretKey = secretKey + ":";
        String encodedAuth = new String(Base64.getEncoder().encode(secretKey.getBytes(StandardCharsets.UTF_8)));

        headers.setBasicAuth(encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        JSONObject params = new JSONObject();
        params.put("cancelReason", order.getOrderNumber());
        params.put("cancelAmount", order.getTotalPaymentAmount());

        String paymentKey = order.getPaymentKey();
        URI uri = URI.create(tossUrl + paymentKey + "/cancel");

        TossPaymentDto tossPaymentDto = restTemplate.postForEntity(
                uri, new HttpEntity<>(params, headers), TossPaymentDto.class
        ).getBody();

        Assert.notNull(tossPaymentDto, "결제 취소 요청에 실패했습니다.");
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
