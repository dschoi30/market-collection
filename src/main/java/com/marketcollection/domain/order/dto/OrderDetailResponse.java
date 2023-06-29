package com.marketcollection.domain.order.dto;

import com.marketcollection.domain.delivery.DeliveryStatus;
import com.marketcollection.domain.payment.PaymentType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class OrderDetailResponse {
    private String orderNumber;

    private List<OrderDetailItemDto> orderDetailItems;

    private int totalItemPrice;
    private int totalOrderPrice;
    private int totalDiscountPrice;
    private int shippingCost;
    private int usingPoint;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    private LocalDateTime paymentApprovedAt;

    private String sender;
    private String recipient;
    private String phoneNumber;
    private int zipCode;
    private String address;
    private String addressDetail;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @QueryProjection
    public OrderDetailResponse(String orderNumber, int usingPoint, PaymentType paymentType, LocalDateTime paymentApprovedAt,
                               String sender, String recipient, String phoneNumber,
                               int zipCode, String address, String addressDetail, DeliveryStatus deliveryStatus) {
        this.orderNumber = orderNumber;
        this.usingPoint = usingPoint;
        this.paymentType = paymentType;
        this.paymentApprovedAt = paymentApprovedAt;
        this.sender = sender;
        this.recipient = recipient;
        this.phoneNumber = phoneNumber;
        this.zipCode = zipCode;
        this.address = address;
        this.addressDetail = addressDetail;
        this.deliveryStatus = deliveryStatus;
    }

    public void setOrderDetailItems(List<OrderDetailItemDto> orderDetailItems) {
        this.orderDetailItems = orderDetailItems;
        setTotalPrice(orderDetailItems);
    }

    private void setTotalPrice(List<OrderDetailItemDto> orderDetailItemDtos) {
        this.totalOrderPrice = orderDetailItemDtos.stream()
                .mapToInt(o -> o.getOrderPrice() * o.getCount())
                .sum();
        this.totalItemPrice = orderDetailItemDtos.stream()
                .mapToInt(o -> o.getOriginalPrice() * o.getCount())
                .sum();
        this.totalDiscountPrice = totalItemPrice - totalOrderPrice;
    }
}
