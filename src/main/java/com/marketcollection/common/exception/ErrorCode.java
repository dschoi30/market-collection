package com.marketcollection.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    BINDING_WRONG(HttpStatus.BAD_REQUEST.value(), "요청 필드 값이 유효하지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 회원이 존재하지 않습니다"),
    OUT_OF_STOCK(HttpStatus.BAD_REQUEST.value(), "상품의 재고가 부족합니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED.value(), "로그인 후 이용 가능합니다."),
    INVALID_PAYMENT_AMOUNT(HttpStatus.BAD_REQUEST.value(), "결제 금액이 유효하지 않습니다.");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
