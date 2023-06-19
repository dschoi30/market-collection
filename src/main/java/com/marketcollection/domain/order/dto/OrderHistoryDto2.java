package com.marketcollection.domain.order.dto;

import com.marketcollection.domain.order.OrderStatus;
import com.marketcollection.domain.payment.PaymentType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class OrderHistoryDto2 {
    private String orderNumber;
    private String orderName;
    private PaymentType paymentType;
    private int totalPaymentAmount;
    private String orderDate;
    private String repImage;
    private OrderStatus orderStatus;

    @QueryProjection
    public OrderHistoryDto2(String orderNumber, String orderName, PaymentType paymentType,
                            int totalPaymentAmount, LocalDateTime orderDate, String repImage, OrderStatus orderStatus) {
        this.orderNumber = orderNumber;
        this.orderName = orderName;
        this.paymentType = paymentType;
        this.totalPaymentAmount = totalPaymentAmount;
        this.orderDate = orderDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd (HH시 mm분)"));
        this.repImage = repImage;
        this.orderStatus = orderStatus;
    }
}
