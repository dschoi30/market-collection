package com.marketcollection.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketcollection.domain.common.Address;
import com.marketcollection.domain.common.BaseEntity;
import com.marketcollection.domain.member.Member;
import com.marketcollection.domain.order.dto.OrderDto;
import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private String phoneNumber;

    @Embedded
    private Address address;

    private int totalSavingPoint;
    private int usingPoint;
    private int totalPaymentAmount;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Order(Member member, List<OrderItem> orderItems, String phoneNumber, Address address, OrderStatus orderStatus) {
        this.member = member;
        this.orderItems = orderItems;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.orderStatus = orderStatus;
    }

    public static Order createOrder(Member member, List<OrderItem> orderItems, OrderDto orderDto) {
        int savingPoint = orderItems.stream().mapToInt(OrderItem::getSavingPoint).sum();
        int orderAmount = orderItems.stream().mapToInt(OrderItem::getOrderPrice).sum();
        return Order.builder()
                .member(member)
                .phoneNumber(member.getPhoneNumber())
                .address(member.getAddress())
                .orderItems(orderItems)
                .totalSavingPoint(savingPoint)
                .usingPoint(orderDto.getUsingPoint())
                .totalPaymentAmount(orderAmount - orderDto.getUsingPoint())
                .orderStatus(OrderStatus.ORDERED)
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
}
