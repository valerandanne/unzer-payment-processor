package com.unzer.payments.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Contains the handling for each custom exception
 */
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<String> handlePaymentNotFoundException(PaymentNotFoundException paymentNotFoundException){
        return new ResponseEntity<>(paymentNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }
}
