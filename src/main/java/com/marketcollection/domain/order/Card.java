package com.marketcollection.domain.order;

import com.marketcollection.domain.order.dto.TossCardDto;
import com.marketcollection.domain.order.dto.TossPaymentDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Card {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String issuerCode; // 카드 발급사 코드
    private String number; // 카드 번호
    private String approveNo; // 카드사 승인 번호
    private String cardType; // 카드 종류(신용, 체크, 기프트, 미확인)
}