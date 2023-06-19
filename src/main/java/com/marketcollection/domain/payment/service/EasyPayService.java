package com.marketcollection.domain.payment.service;

import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.payment.PaymentType;
import com.marketcollection.domain.order.dto.PGResponseDto;
import com.marketcollection.domain.payment.repository.EasyPayRepository;
import com.marketcollection.domain.payment.EasyPay;
import com.marketcollection.domain.payment.Payment;
import com.marketcollection.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class EasyPayService implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final EasyPayRepository easyPayRepository;

    @Override
    @Transactional
    public Payment savePayment(PGResponseDto pgResponseDto) {
        Payment payment = Payment.createPayment(pgResponseDto, PaymentType.EASY_PAY);
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public void savePaymentMethod(PGResponseDto pgResponseDto, Order order) {
        EasyPay easyPay = pgResponseDto.getEasyPay();
        easyPay.setOrder(order);
        easyPayRepository.save(easyPay);
    }
}
