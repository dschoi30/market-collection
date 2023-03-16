package com.marketcollection.domain.order.exception;

import com.marketcollection.common.exception.BusinessException;
import com.marketcollection.common.exception.ErrorCode;

public class OutOfStockException extends BusinessException {
    public OutOfStockException(ErrorCode errorCode) {
        super(errorCode);
    }
}
