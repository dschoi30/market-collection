package com.marketcollection.domain.order;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Card {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String issuerCode; // 카드 발급사 코드
    private String acquirerCode; // 카드 매입사 코드
    private String number; // 카드 번호(마스킹 처리 후 저장)
    private String approveNo; // 카드사 승인 번호
    private String cardType; // 카드 종류(신용, 체크, 기프트, 미확인)
    private Integer installmentPlanMonths; // 할부 개월 수(일시불은 0)
    private String acquireStatus; // 매입 상태(READY, REQUESTED, COMPLETED, CANCEL_REQUESTED, CANCELED)

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public void setOrder(Order order) {
        this.order = order;
    }
}