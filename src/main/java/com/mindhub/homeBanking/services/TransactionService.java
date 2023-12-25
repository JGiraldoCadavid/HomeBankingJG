package com.mindhub.homeBanking.services;

import com.mindhub.homeBanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    void saveTransaction(Transaction transaction);
    List<Transaction> findByAccount_idAndDateBetweenOrderByDateDesc(Long id, LocalDateTime from, LocalDateTime until);
    Transaction findTransactionById(Long id);
}
