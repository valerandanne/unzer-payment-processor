package com.unzer.payments.service;

import com.unzer.payments.model.PaymentConfirmation;
import com.unzer.payments.model.PaymentRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service that acts as a client of the external payment processor
 */
@Service
public class ExternalPaymentProcessorClientImpl implements PaymentProcessorClient {

    private final RestTemplate restTemplate;

    @Value("${external-processor-config.url}")
    private String externalProcessorUrl;

    ExternalPaymentProcessorClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public PaymentConfirmation processPayment(PaymentRequest paymentRequest) {
        PaymentConfirmation paymentConfirmation = null;
        try {
            paymentConfirmation = restTemplate.postForObject(externalProcessorUrl + "/process", paymentRequest,
                    PaymentConfirmation.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentConfirmation;
    }
}
