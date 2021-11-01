package com.unzer.payments.model;

/**
 * The possible statuses that a {@link PaymentResponseDto} can have
 */
public enum PaymentStatus {

    APPROVED("Approved"),
    CANCELED("Canceled"),
    FAILED("Failed");

    PaymentStatus(String statusDescription) {
    }
}
