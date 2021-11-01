package com.unzer.payments.exception;

/**
 * Custom exception to point out that the requested {@link com.unzer.payments.entity.Payment} does not exist
 */
public class PaymentNotFoundException extends Exception {

    public PaymentNotFoundException(String message) {
        super(message);
    }
}
