package com.marketcollection.domain.payment.service;

import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.payment.PaymentType;
import com.marketcollection.domain.payment.Transfer;
import com.marketcollection.domain.order.dto.PGResponseDto;
import com.marketcollection.domain.payment.repository.TransferRepository;
import com.marketcollection.domain.payment.Payment;
import com.marketcollection.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TransferService implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final TransferRepository transferRepository;

    @Override
    @Transactional
    public Payment savePayment(PGResponseDto pgResponseDto) {
        Payment payment = Payment.createPayment(pgResponseDto, PaymentType.TRANSFER);
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public void savePaymentMethod(PGResponseDto pgResponseDto, Order order) {
        Transfer transfer = pgResponseDto.getTransfer();
        transfer.setOrder(order);
        transferRepository.save(transfer);
    }
}
