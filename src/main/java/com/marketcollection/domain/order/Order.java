package com.marketcollection.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketcollection.common.entity.Address;
import com.marketcollection.domain.user.User;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter
@Table(name = "orders")
@Entity
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private int count;

    @Embedded
    private Address address;

    private OrderStatus orderStatus;
}
