package com.marketcollection.common.exception;

public class BusinessException extends RuntimeException {
    private final int status;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.status = errorCode.getStatus();
    }

    public int getStatus() {
        return status;
    }
}