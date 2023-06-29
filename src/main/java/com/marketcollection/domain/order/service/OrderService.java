package com.marketcollection.domain.order.service;

import com.marketcollection.domain.cart.service.CartService;
import com.marketcollection.domain.common.PageCursor;
import com.marketcollection.domain.delivery.Delivery;
import com.marketcollection.domain.delivery.repository.DeliveryRepository;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.repository.ItemRepository;
import com.marketcollection.domain.member.Member;
import com.marketcollection.domain.member.repository.MemberRepository;
import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.OrderCancel;
import com.marketcollection.domain.order.OrderItem;
import com.marketcollection.domain.order.dto.*;
import com.marketcollection.domain.order.repository.OrderCancelRepository;
import com.marketcollection.domain.order.repository.OrderItemRepository;
import com.marketcollection.domain.order.repository.OrderRepository;
import com.marketcollection.domain.order.dto.PaymentResponse;
import com.marketcollection.domain.order.dto.PGResponse;
import com.marketcollection.domain.payment.Payment;
import com.marketcollection.domain.payment.repository.PaymentRepository;
import com.marketcollection.domain.payment.service.PaymentContext;
import com.marketcollection.domain.payment.service.PaymentService;
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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderCancelRepository orderCancelRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentContext paymentContext;
    private final CartService cartService;
    private final PointService pointService;

    @Value("${tossSecretKey}")
    private String secretKey;
    private final String tossUrl = "https://api.tosspayments.com/v1/payments/";

    /**
     * 주문 정보 생성
     */
    @Transactional(readOnly = true)
    public OrderDto setOrderInfo(String memberId, OrderRequest orderRequest, String directOrderYn) {
        OrderDto orderDto = new OrderDto();

        // 주문자 정보
        Member member = memberRepository.findByEmail(memberId).orElseThrow(EntityNotFoundException::new);
        orderDto.setMemberInfo(member);

        // 주문 상품 정보
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        List<OrderItemRequest> orderItemRequests = orderRequest.getOrderItemRequests();

        List<Long> itemIds = orderItemRequests.stream()
                .map(OrderItemRequest::getItemId)
                .collect(Collectors.toList());
        List<Item> items = itemRepository.findAllById(itemIds);

        for (OrderItemRequest orderItemRequest : orderItemRequests) {
            Item item = items.stream()
                    .filter(i -> i.getId().equals(orderItemRequest.getItemId()))
                    .findFirst()
                    .orElseThrow(EntityNotFoundException::new);

            OrderItemDto orderItemDto = new OrderItemDto(item, orderItemRequest.getCount(),
                    member.getGrade().getPointSavingRate());
            orderItemDtos.add(orderItemDto);
        }
        orderDto.setOrderItemInfo(orderItemDtos);
        orderDto.setDirectOrderYn(directOrderYn);

        return orderDto;
    }

    /**
     * 주문 처리
     */
    @Transactional
    public OrderResponse order(String memberId, OrderDto orderDto) {
        // 배송 정보 등록
        Member member = memberRepository.findByEmail(memberId).orElseThrow(EntityNotFoundException::new);
        member.updateDeliveryInfo(orderDto);
        Delivery delivery = Delivery.createDelivery(orderDto);
        deliveryRepository.save(delivery);

        // 주문 등록
        Order order = handleOrder(member, orderDto);
        order.setDelivery(delivery);
        delivery.setOrder(order);

        // 포인트 입출 내역 등록
        handlePoint(member, order, orderDto);

        // 장바구니 목록 정리
        if(Objects.equals(orderDto.getDirectOrderYn(), "N")) {
            cartService.deleteCartItemsAfterOrder(member.getId(), order.getOrderItems());
        }

        return order.toDto();
    }

    private Order handleOrder(Member member, OrderDto orderDto) {
        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderItemDto> orderItemDtos = orderDto.getOrderItemDtos();

        List<Long> itemIds = orderItemDtos.stream()
                .map(OrderItemDto::getItemId)
                .collect(Collectors.toList());
        List<Item> items = itemRepository.findAllWithPessimisticLockById(itemIds);

        for (OrderItemDto orderItemDto : orderItemDtos) {
            Item item = items.stream()
                    .filter(i -> i.getId().equals(orderItemDto.getItemId()))
                    .findFirst()
                    .orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item, orderItemDto.getCount(),
                    member.getGrade().getPointSavingRate());
            orderItems.add(orderItem);
        }

        Order order = Order.createOrder(member, orderItems, orderDto);
        orderItems.forEach(oi -> oi.setOrder(order));
        orderRepository.save(order);

        return order;
    }

    private void handlePoint(Member member, Order order, OrderDto orderDto) {
        member.updateOrderPoint(orderDto.getTotalSavingPoint(), orderDto.getUsingPoint());
        order.getOrderItems().forEach(oi ->
            pointService.createOrderPoint(member, oi));
        if (orderDto.getUsingPoint() > 0) {
            pointService.createUsingPoint(member, orderDto);
        }
    }

    /**
     * 결제 처리
     */
    @Transactional
    public PaymentResponse handlePayment(String paymentKey, String orderId, Long amount) {
        // 결제 승인 요청
        PGResponse pgResponse = requestPaymentApproval(paymentKey, orderId, amount);

        // 결제 수단별 결제 정보 저장
        PaymentService paymentService = paymentContext.getPaymentService(pgResponse);
        Payment payment = paymentService.savePayment(pgResponse);

        Order order = orderRepository.findByOrderNumber(orderId).orElseThrow(EntityNotFoundException::new);
        order.setPayment(payment);
        payment.setOrder(order);

        paymentService.savePaymentMethod(pgResponse, order);

        return PaymentResponse.of(pgResponse, order.getId());
    }

    private PGResponse requestPaymentApproval(String paymentKey, String orderId, Long amount) {
        RestTemplate restTemplate = new RestTemplate();

        URI uri = URI.create(tossUrl + "confirm");
        JSONObject params = createPaymentParams(orderId, amount, paymentKey);
        HttpHeaders headers = createHeaders();

        PGResponse pgResponse = restTemplate
                .postForEntity(uri, new HttpEntity<>(params, headers), PGResponse.class)
                .getBody();

        Assert.notNull(pgResponse, "결제 승인 요청에 실패했습니다.");

        return pgResponse;
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

    /**
     * 결제 금액 유효성 검사
     */
    @Transactional
    public boolean validatePaymentAmount(String orderId, Long amount) {
        Order order = orderRepository.findByOrderNumber(orderId).orElseThrow(EntityNotFoundException::new);
        boolean isValidAmount = order.getTotalPaymentAmount() == amount;
        if(!isValidAmount) {
            order.failOrder();
        }

        return isValidAmount;
    }

    /**
     * 주문 실패 처리
     */
    @Transactional
    public void abortOrder(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber).orElseThrow(EntityNotFoundException::new);
        order.failOrder();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(OrderCancelDto orderCancelDto) {
        // 결제 취소 요청
        String paymentKey = paymentRepository.findByOrderId(orderCancelDto.getOrderId())
                .orElseThrow(EntityNotFoundException::new)
                .getPaymentKey();
        String cancelReason = orderCancelDto.getCancelReason();
        int cancelAmount = getCancelAmount(orderCancelDto);

        PGResponse pgResponse = requestPaymentCancel(paymentKey, cancelReason, cancelAmount);

        // 주문 취소 처리
        Order order = orderRepository.findById(orderCancelDto.getOrderId()).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder(); // TODO: 부분 취소 여부 확인 후 처리

        // 주문 취소 정보 저장
        OrderCancel orderCancel = OrderCancel.createOrderCancel(pgResponse);
        orderCancel.setOrder(order);
        orderCancelRepository.save(orderCancel);
    }

    private int getCancelAmount(OrderCancelDto orderCancelDto) {
        List<Long> orderItemIds = orderCancelDto.getOrderItemIds();
        List<OrderItem> orderItems = orderItemRepository.findByIdIn(orderItemIds);
        return orderItems.stream()
                .mapToInt(OrderItem::getOrderPrice)
                .sum();
    }

    private PGResponse requestPaymentCancel(String paymentKey, String cancelReason, int cancelAmount) {
        RestTemplate restTemplate = new RestTemplate();

        URI uri = URI.create(tossUrl + paymentKey + "/cancel");

        JSONObject params = createPaymentCancelParams(cancelReason, cancelAmount);
        HttpHeaders headers = createHeaders();

        PGResponse pgResponse = restTemplate
                .postForEntity(uri, new HttpEntity<>(params, headers), PGResponse.class)
                .getBody();

        Assert.notNull(pgResponse, "결제 취소 요청에 실패했습니다.");

        return pgResponse;
    }

    /**
     * 주문자 유효성 검사
     */
    @Transactional(readOnly = true)
    public boolean validateOrder(String orderNumber, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        Order order = orderRepository.findByOrderNumber(orderNumber).orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        return StringUtils.equals(member.getEmail(), savedMember.getEmail());
    }

    /**
     * 내 주문 정보 조회
     */
//    @Transactional(readOnly = true)
//    public Page<OrderHistoryDto> getOrderHistory(String memberId, OrderSearchDto orderSearchDto, Pageable pageable) {
//        Member member = memberRepository.findByEmail(memberId).orElseThrow(EntityNotFoundException::new);
//        List<Order> orders = orderRepository.findOrders(member.getId(), orderSearchDto, pageable);
//        Long total = orderRepository.countOrders(member.getId(), orderSearchDto);
//
//        List<OrderHistoryDto> orderHistoryDtos = new ArrayList<>();
//        for (Order order : orders) {
//            OrderHistoryDto orderHistoryDto = new OrderHistoryDto(order);
//            List<OrderItemDto> orderItemDtos = new ArrayList<>();
//            List<OrderItem> orderItems = order.getOrderItems();
//            for (OrderItem orderItem : orderItems) {
//                OrderItemDto orderItemDto = new OrderItemDto(orderItem);
//                orderItemDtos.add(orderItemDto);
//            }
//            orderHistoryDto.setOrderItemDtos(orderItemDtos);
//            orderHistoryDtos.add(orderHistoryDto);
//        }
//        return new PageImpl<OrderHistoryDto>(orderHistoryDtos, pageable, total);
//    }

    /**
     * 내 주문 내역 조회
     */
    @Transactional(readOnly = true)
    public PageCursor<OrderHistoryDto> getOrderHistory(String email, String searchDateType,
                                                       Long lastOrderId, Pageable pageable) {
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);

        List<OrderHistoryDto> orderHistoryDtos = findByCursorSize(member.getId(), searchDateType, lastOrderId, pageable);
        Long lastOrderIdInList = orderHistoryDtos.isEmpty() ? null :
                orderHistoryDtos.get(orderHistoryDtos.size() - 1).getOrderId();

        return new PageCursor<>(orderHistoryDtos, hasNext(lastOrderIdInList));
    }

    private boolean hasNext(Long id) {
        if(id == null) return false;
        return orderRepository.existsById(id);
    }

    private List<OrderHistoryDto> findByCursorSize(Long memberId, String searchDateType,
                                                   Long cursorItemId, Pageable pageable) {
        return cursorItemId == null ?
                orderRepository.findOrderHistory(memberId, searchDateType, pageable) :
                orderRepository.findOrderHistoryLessThanId(memberId, searchDateType, cursorItemId, pageable);
    }

    /**
     * 내 주문 내역 상세 조회
     */
    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderHistoryDetail(String email, Long orderId) {
        memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);

        OrderDetailResponse orderDetailResponse = orderRepository.findOrderDetail(orderId);

        List<OrderDetailItemDto> orderDetailItemDtos = getOrderDetailItems(orderId);
        orderDetailResponse.setOrderDetailItems(orderDetailItemDtos);

        return orderDetailResponse;
    }

    public List<OrderDetailItemDto> getOrderDetailItems(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        List<OrderDetailItemDto> orderDetailItemDtos = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            OrderDetailItemDto orderDetailItemDto = new OrderDetailItemDto(orderItem);
            orderDetailItemDtos.add(orderDetailItemDto);
        }
        return orderDetailItemDtos;
    }

    /**
     * 관리자 주문 관리
     */
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

    /**
     * 결제 금액 변조 시도 시 결제 취소
     */
    @Transactional
    public void abortPayment(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber).orElseThrow(EntityNotFoundException::new);
        List<Long> orderItemIds = order.getOrderItems().stream().map(OrderItem::getId).collect(Collectors.toList());

        cancelOrder(new OrderCancelDto(order.getId(), orderItemIds, "결제 금액 변조 시도로 인한 주문 취소"));
    }
}
