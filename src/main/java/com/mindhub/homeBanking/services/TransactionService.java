package com.mindhub.homeBanking.services;

import com.mindhub.homeBanking.models.Transaction;

public interface TransactionService {
    void saveTransaction(Transaction transaction);
}
