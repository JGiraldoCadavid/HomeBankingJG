package com.mindhub.homeBanking.services.implement;

import com.mindhub.homeBanking.models.Transaction;
import com.mindhub.homeBanking.repositories.TransactionRepository;
import com.mindhub.homeBanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findByAccount_idAndDateBetweenOrderByDateDesc(Long id, LocalDateTime from, LocalDateTime until) {
        return transactionRepository.findByAccount_idAndDateBetweenOrderByDateDesc(id,from,until);
    }

    @Override
    public Transaction findTransactionById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

}
