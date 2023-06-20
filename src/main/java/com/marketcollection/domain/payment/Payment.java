package com.marketcollection.domain.payment;

import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.dto.PGResponse;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private String paymentKey;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private int totalPaymentAmount;
    private int suppliedAmount;
    private int vat;
    private LocalDateTime paymentApprovedAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public static Payment createPayment(PGResponse pgResponse, PaymentType paymentType) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        return Payment.builder()
                .paymentKey(pgResponse.getPaymentKey())
                .paymentType(paymentType)
                .totalPaymentAmount(pgResponse.getTotalAmount())
                .suppliedAmount(pgResponse.getSuppliedAmount())
                .vat(pgResponse.getVat())
                .paymentApprovedAt(OffsetDateTime.parse(pgResponse.getApprovedAt(), formatter).toLocalDateTime())
                .paymentStatus(PaymentStatus.valueOf(pgResponse.getStatus()))
                .build();
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}