package com.mindhub.homeBanking.dtos;

import java.util.List;

public class CreateLoanDTO {

    private String name;
    private double maxAmount;
    private List<Integer> payments;
    private double interestRate;

    public CreateLoanDTO() {
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
