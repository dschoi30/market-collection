package com.marketcollection.domain.order.service;

import com.marketcollection.domain.order.Card;
import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.dto.PGResponseDto;
import com.marketcollection.domain.order.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CardService implements PaymentService {

    private final CardRepository cardRepository;

    @Override
    public void savePaymentInfo(PGResponseDto tossPaymentDto, Order order) {
        Card card = tossPaymentDto.getCard();
        card.setOrder(order);
        cardRepository.save(card);
    }
}
