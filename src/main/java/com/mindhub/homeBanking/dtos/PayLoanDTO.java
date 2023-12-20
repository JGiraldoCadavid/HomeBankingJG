package com.mindhub.homeBanking.dtos;

public class PayLoanDTO {
    private Long clientLoanId;
    private double amountPaid;
    private int payments;
    private String numberAccount;

    public PayLoanDTO() {
    }

    public Long getClientLoanId() {
        return clientLoanId;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public int getPayments() {
        return payments;
    }

    public String getNumberAccount() {
        return numberAccount;
    }
}

