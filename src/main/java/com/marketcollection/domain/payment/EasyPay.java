package com.marketcollection.domain.payment;

import com.marketcollection.domain.order.Order;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class EasyPay {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private String provider; // 토스페이, 네이버페이, 삼성페이, 애플페이, 엘페이, 카카오페이, 페이코, SSG페이

    public void setOrder(Order order) {
        this.order = order;
        order.setPaymentType(PaymentType.EASY_PAY);
    }
}
