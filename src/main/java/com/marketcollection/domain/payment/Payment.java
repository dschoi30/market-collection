package com.marketcollection.domain.payment;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;
    private String paymentKey;
    private String paymentType;
    private int totalPaymentAmount;
    private int suppliedAmount;
    private int vat;
    private LocalDateTime paymentApprovedAt;
    private PaymentStatus paymentStatus;
}
