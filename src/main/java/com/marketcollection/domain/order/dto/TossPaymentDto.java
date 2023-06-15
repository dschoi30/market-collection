package com.marketcollection.domain.order.dto;

import com.marketcollection.domain.order.Card;
import com.marketcollection.domain.order.Transfer;
import lombok.Getter;

@Getter
public class TossPaymentDto {
    String mId; // 상점아이디
    String version; // Payment 객체의 응답 버전
    String paymentKey; // 결제 키 값
    String orderId; // 주문 번호
    String orderName; // 주문명
    String currency; // 결제 통화
    String method; // 결제 수단(카드, 가상계좌, 간편결제, 휴대폰, 계좌이체, 문화상품권, 도서문화상품권, 게임문화상품권)
    String status; // 결제 처리 상태(READY, IN_PROGRESS, WAITING_FOR_DEPOSIT, DONE, CANCELED, PARTIAL_CANCELED, ABORTED, EXPIRED)
    String requestedAt; // 결제가 일어난 날짜와 시간 정보
    String approvedAt; // 결제 승인이 일어난 날짜와 시간 정보
    String totalAmount; // 총 결제 금액
    Card card; // 카드 결제
    Transfer transfer; // 계좌 이체
}
