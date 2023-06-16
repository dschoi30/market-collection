package com.marketcollection.domain.order.dto;

import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.OrderStatus;
import lombok.*;

import java.time.format.DateTimeFormatter;

@ToString
@Getter @Setter
public class AdminOrderDto {
    private Long orderId;
    private String orderDate;
    private String repItemName;
    private int orderPrice;
    private String repImageUrl;
    private OrderStatus orderStatus;
    private String memberId;

    public void addOrderInfo(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getPaymentApprovedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    public void addMemberInfo(String email) {
        this.memberId = email;
    }

    public void addItemInfo(String repImageUrl, String itemName, int size, int orderPrice) {
        if(size > 1) {
            this.repItemName = itemName + "외 " + (size - 1) + "건";
        } else {
            this.repItemName = itemName;
        }
        this.repImageUrl = repImageUrl;
        this.orderPrice = orderPrice;
    }
}
