package com.mindhub.homeBanking.services;

import com.mindhub.homeBanking.models.Account;

import java.util.List;

public interface AccountService {
    List<Account> findAllAccounts();

    Account findAccountById(Long id);

    void saveAccount(Account account);

    Account findAccountByNumber(String number);

    boolean existsByNumber(String number);
}
