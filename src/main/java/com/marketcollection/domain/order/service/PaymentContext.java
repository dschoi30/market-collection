package com.marketcollection.domain.order.service;

import com.marketcollection.domain.order.dto.PGResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentContext {

    private final CardService cardService;
    private final TransferService transferService;

    public PaymentService getPaymentService(PGResponseDto tossPaymentDto) {
        final PaymentService paymentService;

        switch (tossPaymentDto.getMethod()) {
            case "카드":
                return paymentService = cardService;
            case "계좌이체":
                return paymentService = transferService;
            default:
                throw new IllegalArgumentException("결제 타입이 올바르지 않습니다.");
        }
    }
}
