package com.marketcollection.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<String> handleBindException(BindException e) {
        int httpStatus = ErrorCode.BINDING_WRONG.getStatus();
        String message = ErrorCode.BINDING_WRONG.getMessage();

        return ResponseEntity.status(httpStatus).body(message);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<String> handledBusinessException(BusinessException e) {
        log.info(e.getMessage(), e);
        HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus());
        return ResponseEntity.status(httpStatus).body(e.getMessage());
    }
}
