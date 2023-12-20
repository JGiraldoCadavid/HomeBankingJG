package com.mindhub.homeBanking.services.implement;

import com.mindhub.homeBanking.models.ClientLoan;
import com.mindhub.homeBanking.repositories.ClientLoanRepository;
import com.mindhub.homeBanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanServiceImpl implements ClientLoanService {

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Override
    public boolean existsClientLoanById(Long id) {
        return clientLoanRepository.existsById(id);
    }

    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }

    @Override
    public ClientLoan findClientLoanById(Long id) {
        return clientLoanRepository.findClientLoanById(id);
    }

}
