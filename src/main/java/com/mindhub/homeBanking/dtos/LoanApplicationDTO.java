package com.mindhub.homeBanking.dtos;

public class LoanApplicationDTO {

    private Long id;
    private double amount;
    private int payments;
    private String destinationAccount;

    public LoanApplicationDTO() {
    }

    public Long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

}
