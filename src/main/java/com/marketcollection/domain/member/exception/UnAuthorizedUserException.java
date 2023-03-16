package com.marketcollection.domain.member.exception;

import com.marketcollection.common.exception.BusinessException;
import com.marketcollection.common.exception.ErrorCode;

public class UnAuthorizedUserException extends BusinessException {
    public UnAuthorizedUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
