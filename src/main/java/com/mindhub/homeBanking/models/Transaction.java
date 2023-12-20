package com.mindhub.homeBanking.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double amount;
    private String description;
    private LocalDateTime date;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private double currentBalance;
    private boolean isActive;
    @ManyToOne(fetch = FetchType.EAGER)
    private Account account;

    public Transaction() {
    }
    public Transaction(double amount, String description, LocalDateTime date, TransactionType type, double currentBalance, boolean isActive) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
        this.currentBalance = currentBalance;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account accountID) {
        this.account = accountID;
    }
}
