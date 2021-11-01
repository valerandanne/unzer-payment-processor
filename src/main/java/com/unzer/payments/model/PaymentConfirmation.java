package com.unzer.payments.model;

/**
 * Representation of the response of the external processor API
 */
public class PaymentConfirmation {

    private String approvalCode;

    public PaymentConfirmation() {};

    public PaymentConfirmation(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }
}
