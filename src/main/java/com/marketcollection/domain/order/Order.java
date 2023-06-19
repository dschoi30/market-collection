package com.marketcollection.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketcollection.domain.common.BaseTimeEntity;
import com.marketcollection.domain.delivery.Delivery;
import com.marketcollection.domain.member.Member;
import com.marketcollection.domain.order.dto.OrderDto;
import com.marketcollection.domain.order.dto.OrderResponseDto;
import com.marketcollection.domain.order.dto.PGResponseDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "orders")
@Entity
public class Order extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;
    private String orderName;

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
    private String phoneNumber;

    private int totalSavingPoint;
    private int usingPoint;
    private int totalPaymentAmount;

    /* 결제 완료 후 삽입 될 값 */
    String paymentKey;
    String paymentType;
    LocalDateTime paymentApprovedAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Order(Member member, List<OrderItem> orderItems, String phoneNumber, OrderStatus orderStatus) {
        this.member = member;
        this.orderItems = orderItems;
        this.phoneNumber = phoneNumber;
        this.orderStatus = orderStatus;
    }

    public static Order createOrder(Member member, Delivery delivery, List<OrderItem> orderItems, OrderDto orderDto) {
        String orderNumber = UUID.randomUUID().toString().substring(1,8);

        String orderName;
        if(orderItems.size() > 1) {
            orderName = orderItems.get(0).getItem().getItemName() + "외 " + (orderItems.size() - 1) + "건";
        } else {
            orderName = orderItems.get(0).getItem().getItemName();
        }

        int savingPoint = orderItems.stream().mapToInt(OrderItem::getSavingPoint).sum();
        int orderAmount = orderItems.stream().mapToInt(OrderItem::getOrderPrice).sum();
        return Order.builder()
                .orderNumber(orderNumber)
                .orderName(orderName)
                .orderItems(orderItems)
                .member(member)
                .delivery(delivery)
                .phoneNumber(member.getPhoneNumber())
                .totalSavingPoint(savingPoint)
                .usingPoint(orderDto.getUsingPoint())
                .totalPaymentAmount(orderAmount - orderDto.getUsingPoint())
                .build();
    }

    public int getTotalOrderPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getOrderPrice();
        }
        return totalPrice;
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCELED;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    public void failOrder() {
        this.orderStatus = OrderStatus.ABORTED;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    public OrderResponseDto toDto() {
        String orderName;
        if(orderItems.size() > 1) {
            orderName = orderItems.get(0).getItem().getItemName() + "외 " + (orderItems.size() - 1) + "건";
        } else {
            orderName = orderItems.get(0).getItem().getItemName();
        }
        return OrderResponseDto.builder()
                .orderNumber(orderNumber)
                .orderName(orderName)
                .orderPrice(totalPaymentAmount)
                .memberName(member.getMemberName())
                .memberEmail(member.getEmail())
                .build();
    }

    public Order savePaymentInfo(PGResponseDto tossPaymentDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        this.paymentKey = tossPaymentDto.getPaymentKey();
        this.paymentType = tossPaymentDto.getMethod();
        this.orderStatus = OrderStatus.valueOf(tossPaymentDto.getStatus());
        this.paymentApprovedAt = OffsetDateTime.parse(tossPaymentDto.getApprovedAt(), formatter).toLocalDateTime();
        return this;
    }
}
