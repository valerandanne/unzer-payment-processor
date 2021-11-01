package com.unzer.payments.service;

import com.unzer.payments.model.PaymentConfirmation;
import com.unzer.payments.model.PaymentRequest;

public interface PaymentProcessorClient {

     PaymentConfirmation processPayment(PaymentRequest paymentRequest);
}
