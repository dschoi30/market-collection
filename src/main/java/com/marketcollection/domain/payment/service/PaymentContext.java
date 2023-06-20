package com.marketcollection.domain.payment.service;

import com.marketcollection.domain.order.dto.PGResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentContext {

    private final CardService cardService;
    private final TransferService transferService;
    private final EasyPayService easyPayService;

    public PaymentService getPaymentService(PGResponse tossPaymentDto) {
        final PaymentService paymentService;

        switch (tossPaymentDto.getMethod()) {
            case "카드":
                return paymentService = cardService;
            case "계좌이체":
                return paymentService = transferService;
            case "간편결제":
                return paymentService = easyPayService;
            default:
                throw new IllegalArgumentException("결제 타입이 올바르지 않습니다.");
        }

    }
}
