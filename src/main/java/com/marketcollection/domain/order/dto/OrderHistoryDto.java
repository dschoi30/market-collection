package com.marketcollection.domain.order.dto;

import com.marketcollection.domain.order.OrderStatus;
import com.marketcollection.domain.payment.PaymentType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class OrderHistoryDto {
    private Long orderId;
    private String orderNumber;
    private String orderName;
    private PaymentType paymentType;
    private int totalPaymentAmount;
    private String orderDate;
    private String repImageUrl;
    private OrderStatus orderStatus;

    @QueryProjection
    public OrderHistoryDto(Long orderId, String orderNumber, String orderName, PaymentType paymentType,
                           int totalPaymentAmount, LocalDateTime orderDate, String repImageUrl, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.orderName = orderName;
        this.paymentType = paymentType;
        this.totalPaymentAmount = totalPaymentAmount;
        this.orderDate = orderDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd (HH시 mm분)"));
        this.repImageUrl = repImageUrl;
        this.orderStatus = orderStatus;
    }
}
