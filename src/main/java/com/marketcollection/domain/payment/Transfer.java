package com.marketcollection.domain.payment;

import com.marketcollection.domain.order.Order;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Transfer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bankCode;
    private String settlementStatus; // 정산 상태(INCOMPLETED, COMPLETED)

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public void setOrder(Order order) {
        this.order = order;
        order.setPaymentType(PaymentType.TRANSFER);
    }
}
