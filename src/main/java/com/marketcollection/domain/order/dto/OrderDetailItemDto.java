package com.marketcollection.domain.order.dto;

import com.marketcollection.domain.order.OrderItem;
import com.marketcollection.domain.order.OrderStatus;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class OrderDetailItemDto {
    private Long itemId;
    private String itemName;
    private String itemImageUrl;
    private int originalPrice;
    private int orderPrice;
    private int count;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public OrderDetailItemDto(OrderItem orderItem) {
        this.itemId = orderItem.getItem().getId();
        this.itemName = orderItem.getItem().getItemName();
        this.itemImageUrl = orderItem.getRepImageUrl();
        this.originalPrice = orderItem.getItem().getOriginalPrice();
        this.orderPrice = orderItem.getOrderPrice();
        this.count = orderItem.getCount();
        this.orderStatus = orderItem.getOrderStatus();
    }
}
