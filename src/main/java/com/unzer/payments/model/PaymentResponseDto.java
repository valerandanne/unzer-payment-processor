package com.unzer.payments.model;

/**
 * Model that is an abstraction of the payment that we want to show in our responses
 */
public class PaymentResponseDto {

    private long id;
    private double amount;
    private String currency;
    private PaymentStatus status;
    private String approvalId;

    public PaymentResponseDto(){};

    public PaymentResponseDto(long id, double amount, String currency, PaymentStatus status, String approvalId) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.approvalId = approvalId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(String approvalId) {
        this.approvalId = approvalId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
