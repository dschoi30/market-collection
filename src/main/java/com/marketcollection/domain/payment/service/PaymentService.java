package com.marketcollection.domain.payment.service;

import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.dto.PGResponse;
import com.marketcollection.domain.payment.Payment;

public interface PaymentService {

    Payment savePayment(PGResponse pgResponse);

    void savePaymentMethod(PGResponse pgResponse, Order order);
}
