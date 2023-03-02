package com.marketcollection.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketcollection.domain.common.BaseEntity;
import com.marketcollection.domain.member.Member;
import lombok.*;

import javax.persistence.*;
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

    private int phoneNumber;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Order(Member member, List<OrderItem> orderItems, int phoneNumber, Address address, OrderStatus orderStatus) {
        this.member = member;
        this.orderItems = orderItems;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.orderStatus = orderStatus;
    }

    public static Order createOrder(Member member, List<OrderItem> orderItems) {
        return Order.builder()
                .member(member)
                .phoneNumber(member.getPhoneNumber())
                .address(new Address(member.getAddress().getZipCode(),
                        member.getAddress().getAddress(),
                        member.getAddress().getDetailAddress()))
                .orderItems(orderItems)
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
