package com.mindhub.homeBanking.services;

import com.mindhub.homeBanking.models.Client;

import java.util.List;

public interface ClientService {
    Client findClientByEmail(String email);

    List<Client> findAllClients();

    Client findClientById(Long id);

    void saveClient(Client client);

    boolean existsClientByEmail(Long id);
}
