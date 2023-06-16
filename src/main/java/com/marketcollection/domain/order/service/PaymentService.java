package com.marketcollection.domain.order.service;

import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.dto.PGResponseDto;

public interface PaymentService {

    void savePaymentInfo(PGResponseDto tossPaymentDto, Order order);

}
