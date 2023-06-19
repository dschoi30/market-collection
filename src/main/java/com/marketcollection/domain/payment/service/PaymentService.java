package com.marketcollection.domain.payment.service;

import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.dto.PGResponseDto;
import com.marketcollection.domain.payment.Payment;

public interface PaymentService {

    Payment savePayment(PGResponseDto tossPaymentDto);

    void savePaymentMethod(PGResponseDto tossPaymentDto, Order order);
}
