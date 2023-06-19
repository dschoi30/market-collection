package com.marketcollection.domain.order.dto;

import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class OrderHistoryDto {
    private String orderNumber;
    private String orderDate;
    private OrderStatus orderStatus;
    private List<OrderItemDto> orderItemDtos = new ArrayList<>();

    public OrderHistoryDto(Order order) {
        this.orderNumber = order.getOrderNumber();
        this.orderDate = order.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }
}
