package com.mindhub.homeBanking.dtos;

import com.mindhub.homeBanking.models.Account;
import com.mindhub.homeBanking.models.Card;
import com.mindhub.homeBanking.models.Client;
import com.mindhub.homeBanking.models.ClientLoan;

import java.util.List;
import java.util.stream.Collectors;

public class ClientDTO {

    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private List<CardDTO> cards;
    private List<ClientLoanDTO> clientLoans;
    private List<AccountDTO> accounts;

    public ClientDTO(Client client) {
        id = client.getId();
        name = client.getName();
        lastName = client.getLastName();
        email = client.getEmail();
        password = client.getPassword();
        cards = client.getCards().stream().filter(card -> card.isActive()).map(card -> new CardDTO(card)).collect(Collectors.toList());
        clientLoans = client.getLoans().stream().filter(clientLoan -> clientLoan.isActive()).map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(Collectors.toList());
        accounts = client.getAccounts().stream().filter(account -> account.isActive()).map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<CardDTO> getCards() {
        return cards;
    }

    public List<ClientLoanDTO> getClientLoans() {
        return clientLoans;
    }

    public List<AccountDTO> getAccounts() {
        return accounts;
    }
}
