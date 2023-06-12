package com.marketcollection.domain.payment.service;

import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.repository.OrderRepository;
import com.marketcollection.domain.payment.Payment;
import com.marketcollection.domain.payment.dto.TossPaymentDto;
import com.marketcollection.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

@RequiredArgsConstructor
@Transactional
@Service
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Value("${tossSecretKey}")
    private String secretKey;
    private String url = "https://api.tosspayments.com/v1/payments/confirm";

    // 결제 승인 요청
    public String requestPaymentApproval(String paymentKey, String orderId, Long amount) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        secretKey = secretKey + ":";
        String encodedAuth = new String(Base64.getEncoder().encode(secretKey.getBytes(StandardCharsets.UTF_8)));

        headers.setBasicAuth(encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        JSONObject params = new JSONObject();
        params.put("orderId", orderId);
        params.put("amount", amount);
        params.put("paymentKey", paymentKey);

        TossPaymentDto tossPaymentDto = restTemplate.postForEntity(
                url, new HttpEntity<>(params, headers), TossPaymentDto.class
        ).getBody();

        Assert.notNull(tossPaymentDto, "결제 승인 요청에 실패했습니다.");
        Payment payment = Payment.createPayment(tossPaymentDto);
        paymentRepository.save(payment);

        return tossPaymentDto.getTotalAmount();
    }

    // 결제 금액 유효성 검사
    @Transactional
    public boolean validatePaymentAmount(String orderId, Long amount) {
        Order order = orderRepository.findByOrderNumber(orderId).orElseThrow(EntityNotFoundException::new);
        boolean isValidAmount = order.getTotalPaymentAmount() == amount;
        if(!isValidAmount) {
            order.failOrder();
        }

        return isValidAmount;
    }

}
