package com.mindhub.homeBanking.services;

import com.mindhub.homeBanking.models.ClientLoan;

public interface ClientLoanService {
    boolean existsClientLoanById(Long id);
    void saveClientLoan(ClientLoan clientLoan);
    ClientLoan findClientLoanById(Long id);
}
