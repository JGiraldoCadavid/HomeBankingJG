package com.mindhub.homeBanking.services.implement;

import com.mindhub.homeBanking.models.Loan;
import com.mindhub.homeBanking.repositories.LoanRepository;
import com.mindhub.homeBanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public List<Loan> findAllLoans() {
        return loanRepository.findAll();
    }

    @Override
    public Loan findLoanById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existsLoanByNameIgnoreCase(String name) {
        return loanRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }

}
