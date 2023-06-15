package com.marketcollection.domain.order.dto;

import lombok.Getter;

@Getter
public class TossCardDto {
    private int amount;
    private String issuerCode; // 카드 발급사 코드
    private String number; // 카드 번호
    private String approveNo; // 카드사 승인 번호
    private String cardType; // 카드 종류(신용, 체크, 기프트, 미확인)
}
