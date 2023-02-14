package com.marketcollection.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketcollection.domain.common.BaseEntity;
import com.marketcollection.common.entity.Address;
import com.marketcollection.domain.member.Member;
import com.marketcollection.domain.order.dto.OrderDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
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

    private OrderStatus orderStatus;

    public static Order createOrder(Member member, List<OrderItem> orderItems) {
        return Order.builder()
                .member(member)
                .phoneNumber(member.getPhoneNumber())
                .address(new Address(member.getAddress().getZipCode(),
                        member.getAddress().getAddress(),
                        member.getAddress().getDetailAddress()))
                .orderItems(orderItems)
                .orderStatus(OrderStatus.READY)
                .build();
    }

}
