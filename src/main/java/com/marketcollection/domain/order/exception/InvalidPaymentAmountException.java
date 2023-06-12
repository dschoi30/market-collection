package com.marketcollection.domain.order.exception;

import com.marketcollection.common.exception.BusinessException;
import com.marketcollection.common.exception.ErrorCode;

public class InvalidPaymentAmountException extends BusinessException {
    public InvalidPaymentAmountException(ErrorCode errorCode) {
        super(errorCode);
    }
}
