package com.marketcollection.domain.review.exception;

import com.marketcollection.common.exception.BusinessException;
import com.marketcollection.common.exception.ErrorCode;

public class ReviewBadReqeustException extends BusinessException {
    public ReviewBadReqeustException(ErrorCode errorCode) {
        super(errorCode);
    }
}
