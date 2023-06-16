package com.marketcollection.domain.order.service;

import com.marketcollection.domain.order.Card;
import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.Transfer;
import com.marketcollection.domain.order.dto.PGResponseDto;
import com.marketcollection.domain.order.repository.CardRepository;
import com.marketcollection.domain.order.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TransferService implements PaymentService {

    private final TransferRepository transferRepository;

    @Override
    public void savePaymentInfo(PGResponseDto tossPaymentDto, Order order) {
        Transfer transfer = tossPaymentDto.getTransfer();
        transfer.setOrder(order);
        transferRepository.save(transfer);
    }
}
