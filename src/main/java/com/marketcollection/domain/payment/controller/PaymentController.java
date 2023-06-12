package com.marketcollection.domain.payment.controller;

import com.marketcollection.common.auth.LoginUser;
import com.marketcollection.common.auth.dto.SessionUser;
import com.marketcollection.common.exception.ErrorCode;
import com.marketcollection.domain.category.dto.ItemCategoryDto;
import com.marketcollection.domain.category.service.CategoryService;
import com.marketcollection.domain.common.HeaderInfo;
import com.marketcollection.domain.order.exception.InvalidPaymentAmountException;
import com.marketcollection.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class PaymentController extends HeaderInfo {

    private final PaymentService paymentService;
    private final CategoryService categoryService;

    // 결제 처리
    @GetMapping("/order/checkout/success")
    public String handlePayment(Model model, @LoginUser SessionUser user, String paymentKey, String orderId, Long amount) {
        // 카테고리
        ItemCategoryDto itemCategoryDto = categoryService.createCategoryRoot();
        model.addAttribute("itemCategoryDto", itemCategoryDto);

        if(!paymentService.validatePaymentAmount(orderId, amount)) {
            throw new InvalidPaymentAmountException(ErrorCode.INVALID_PAYMENT_AMOUNT);
        };

        try {
            String paymentAmount = paymentService.requestPaymentApproval(paymentKey, orderId, amount);

            model.addAttribute("paymentAmount", paymentAmount);
            model.addAttribute("user", user);

            return "payment/paymentResult";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/order/checkout/fail")
    public String fail() {
        return "payment/fail";
    }

}
