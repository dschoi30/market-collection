package com.marketcollection.domain.delivery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketcollection.domain.common.Address;
import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.dto.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Delivery {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    private String recipient;
    private String phoneNumber;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    public static Delivery createDelivery(OrderDto orderDto) {
        return Delivery.builder()
                .phoneNumber(orderDto.getPhoneNumber())
                .address(new Address(orderDto.getZipCode(), orderDto.getAddress(), orderDto.getDetailAddress()))
                .deliveryStatus(DeliveryStatus.READY)
                .build();
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
