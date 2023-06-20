package com.marketcollection.domain.payment.service;

import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.payment.PaymentType;
import com.marketcollection.domain.payment.Transfer;
import com.marketcollection.domain.order.dto.PGResponse;
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
    public Payment savePayment(PGResponse pgResponse) {
        Payment payment = Payment.createPayment(pgResponse, PaymentType.TRANSFER);
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public void savePaymentMethod(PGResponse pgResponse, Order order) {
        Transfer transfer = pgResponse.getTransfer();
        transfer.setOrder(order);
        transferRepository.save(transfer);
    }
}
