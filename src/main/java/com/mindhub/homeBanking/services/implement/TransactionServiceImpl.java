package com.mindhub.homeBanking.services.implement;

import com.mindhub.homeBanking.models.Transaction;
import com.mindhub.homeBanking.repositories.TransactionRepository;
import com.mindhub.homeBanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

}
