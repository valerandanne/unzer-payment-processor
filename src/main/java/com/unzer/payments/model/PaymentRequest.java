package com.unzer.payments.model;

import java.time.LocalDateTime;

/**
 * Model that represents the data received by the client that wants to create a payment
 */
public class PaymentRequest {

    private String cardNumber;
    private LocalDateTime cardExpiryDate;
    private int cardCvc;
    private Double amount;
    private String currency;

    public PaymentRequest(){}

    public PaymentRequest(String cardNumber, LocalDateTime expiryDate, int cardCvc, Double amount, String currency){
        this.amount = amount;
        this.cardCvc = cardCvc;
        this.cardExpiryDate = expiryDate;
        this.currency = currency;
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDateTime getCardExpiryDate() {
        return cardExpiryDate;
    }

    public void setCardExpiryDate(LocalDateTime cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }

    public int getCardCvc() {
        return cardCvc;
    }

    public void setCardCvc(int cardCvc) {
        this.cardCvc = cardCvc;
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
}
