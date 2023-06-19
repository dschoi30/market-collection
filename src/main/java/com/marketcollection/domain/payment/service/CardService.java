package com.marketcollection.domain.payment.service;

import com.marketcollection.domain.payment.Card;
import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.payment.PaymentType;
import com.marketcollection.domain.order.dto.PGResponseDto;
import com.marketcollection.domain.payment.repository.CardRepository;
import com.marketcollection.domain.payment.Payment;
import com.marketcollection.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CardService implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CardRepository cardRepository;

    @Override
    @Transactional
    public Payment savePayment(PGResponseDto pgResponseDto) {
        Payment payment = Payment.createPayment(pgResponseDto, PaymentType.CARD);
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public void savePaymentMethod(PGResponseDto pgResponseDto, Order order) {
        Card card = pgResponseDto.getCard();
        card.setOrder(order);
        cardRepository.save(card);
    }
}
