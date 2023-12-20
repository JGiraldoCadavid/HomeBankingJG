package com.mindhub.homeBanking.services;

import com.mindhub.homeBanking.models.Loan;

import java.util.List;

public interface LoanService {
    List<Loan> findAllLoans();

    Loan findLoanById(Long id);

    boolean existsLoanByNameIgnoreCase(String name);

    void saveLoan(Loan loan);
}
