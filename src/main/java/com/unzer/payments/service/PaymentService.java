package com.unzer.payments.service;

import com.unzer.payments.entity.Payment;
import com.unzer.payments.exception.PaymentNotFoundException;
import com.unzer.payments.model.PaymentRequest;
import com.unzer.payments.model.PaymentResponseDto;

import java.util.Optional;

public interface PaymentService {

    PaymentResponseDto processPayment(PaymentRequest payment);
    void cancelPayment(long paymentId) throws PaymentNotFoundException;
    Optional<PaymentResponseDto> getPayment(long paymentId);
}
