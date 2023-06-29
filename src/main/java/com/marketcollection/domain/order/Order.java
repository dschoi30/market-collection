package com.marketcollection.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketcollection.domain.common.BaseTimeEntity;
import com.marketcollection.domain.delivery.Delivery;
import com.marketcollection.domain.member.Member;
import com.marketcollection.domain.order.dto.OrderDto;
import com.marketcollection.domain.order.dto.OrderResponse;
import com.marketcollection.domain.payment.Payment;
import com.marketcollection.domain.payment.PaymentType;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "orders")
@Entity
public class Order extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;
    private String orderName;
    private String repImageUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private int totalSavingPoint;
    private int usingPoint;
    private int totalPaymentAmount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Order(Member member, List<OrderItem> orderItems, OrderStatus orderStatus) {
        this.member = member;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
    }

    public static Order createOrder(Member member, List<OrderItem> orderItems, OrderDto orderDto) {
        String orderNumber = UUID.randomUUID().toString().substring(1,8);

        String orderName;
        if(orderItems.size() > 1) {
            orderName = orderItems.get(0).getItem().getItemName() + "외 " + (orderItems.size() - 1) + "건";
        } else {
            orderName = orderItems.get(0).getItem().getItemName();
        }
        String repImageUrl = orderItems.get(0).getRepImageUrl();
        int savingPoint = orderItems.stream().mapToInt(OrderItem::getSavingPoint).sum();
        int orderAmount = orderItems.stream().mapToInt(OrderItem::getOrderPrice).sum();
        return Order.builder()
                .orderNumber(orderNumber)
                .orderName(orderName)
                .repImageUrl(repImageUrl)
                .orderItems(orderItems)
                .member(member)
                .totalSavingPoint(savingPoint)
                .usingPoint(orderDto.getUsingPoint())
                .totalPaymentAmount(orderAmount - orderDto.getUsingPoint())
                .orderStatus(OrderStatus.READY)
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

    public void failOrder() {
        this.orderStatus = OrderStatus.ABORTED;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    public OrderResponse toDto() {
        return OrderResponse.builder()
                .orderNumber(orderNumber)
                .orderName(orderName)
                .orderPrice(totalPaymentAmount)
                .memberName(member.getMemberName())
                .memberEmail(member.getEmail())
                .build();
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
}
