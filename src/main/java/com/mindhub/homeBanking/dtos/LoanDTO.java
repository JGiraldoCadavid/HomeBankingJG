package com.mindhub.homeBanking.dtos;

import com.mindhub.homeBanking.models.Loan;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;

import java.util.ArrayList;
import java.util.List;

public class LoanDTO {

    private Long id;
    private String name;
    private double maxAmount;
    private List<Integer> payments;
    private double interestRate;

    public LoanDTO(Loan loan) {

        this.id= loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
        this.interestRate = loan.getInterestRate();

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public double getInterestRate() {
        return interestRate;
    }
}
