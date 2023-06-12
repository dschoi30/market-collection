package com.marketcollection.domain.payment;

import com.marketcollection.domain.payment.dto.TossPaymentDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String mId; // 상점아이디
    String version; // Payment 객체의 응답 버전
    String paymentKey; // 결제 키 값
    String orderNumber; // 주문 번호
    String orderName; // 주문명
    String currency; // 결제 통화
    String method; // 결제 수단(카드, 가상계좌, 간편결제, 휴대폰, 계좌이체, 문화상품권, 도서문화상품권, 게임문화상품권)
    String status; // 결제 처리 상태(READY, IN_PROGRESS, WAITING_FOR_DEPOSIT, DONE, CANCELED, PARTIAL_CANCELED, ABORTED, EXPIRED)
    String requestedAt; // 결제가 일어난 날짜와 시간 정보
    String approvedAt; // 결제 승인이 일어난 날짜와 시간 정보
    String totalAmount; // 총 결제 금액

    public static Payment createPayment(TossPaymentDto tossPaymentDto) {
        return Payment.builder()
                .mId(tossPaymentDto.getMId())
                .version(tossPaymentDto.getVersion())
                .paymentKey(tossPaymentDto.getPaymentKey())
                .orderNumber(tossPaymentDto.getOrderId())
                .orderName(tossPaymentDto.getOrderName())
                .currency(tossPaymentDto.getCurrency())
                .method(tossPaymentDto.getMethod())
                .status(tossPaymentDto.getStatus())
                .requestedAt(tossPaymentDto.getRequestedAt())
                .approvedAt(tossPaymentDto.getApprovedAt())
                .totalAmount(tossPaymentDto.getTotalAmount())
                .build();
    }
}
