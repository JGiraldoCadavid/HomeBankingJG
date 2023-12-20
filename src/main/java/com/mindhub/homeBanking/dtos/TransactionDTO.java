package com.mindhub.homeBanking.dtos;

import com.mindhub.homeBanking.models.Transaction;
import com.mindhub.homeBanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {

    private Long id;
    private double amount;
    private String description;
    private LocalDateTime date;
    private TransactionType type;
    private boolean isActive;
    private double currentBalance;

    public TransactionDTO(Transaction transaction) {
        id = transaction.getId();
        amount = transaction.getAmount();
        description = transaction.getDescription();
        date = transaction.getDate();
        type = transaction.getType();
        isActive = transaction.isActive();
        currentBalance = transaction.getCurrentBalance();
    }

    public Long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    public boolean isActive() {
        return isActive;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }
}
