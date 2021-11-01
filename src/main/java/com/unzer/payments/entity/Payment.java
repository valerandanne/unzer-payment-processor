package com.unzer.payments.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PAYMENT")
public class Payment {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private double amount;
    private String currency;
    private String status;
    private String approvalId;
    private LocalDateTime creationDate;
    protected Payment() {}

    public Payment(double amount, String currency,String status, LocalDateTime creationDate, String approvalId) {
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.creationDate = creationDate;
        this.approvalId = approvalId;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(String approvalId) {
        this.approvalId = approvalId;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public long getId() {
        return id;
    }
}