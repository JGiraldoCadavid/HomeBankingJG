package com.mindhub.homeBanking.dtos;

import com.mindhub.homeBanking.models.ClientLoan;

public class ClientLoanDTO {
    private Long id;
    private Long loanId;
    private String name;
    private double amount;
    private int payments;
    private double amountPaid;
    private int paymentsPaid;
    private boolean isActive;

    public ClientLoanDTO(ClientLoan clientLoan) {
        id = clientLoan.getId();
        loanId = clientLoan.getLoan().getId();
        name = clientLoan.getLoan().getName();
        amount = clientLoan.getAmount();
        payments = clientLoan.getPayments();
        amountPaid = clientLoan.getAmountPaid();
        paymentsPaid = clientLoan.getPaymentsPaid();
        isActive = clientLoan.isActive();
    }

    public Long getId() {
        return id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public int getPaymentsPaid() {
        return paymentsPaid;
    }

    public boolean isActive() {
        return isActive;
    }
}
