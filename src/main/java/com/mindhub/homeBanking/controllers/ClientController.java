package com.mindhub.homeBanking.controllers;

import com.mindhub.homeBanking.dtos.ClientDTO;
import com.mindhub.homeBanking.models.Account;
import com.mindhub.homeBanking.models.AccountType;
import com.mindhub.homeBanking.models.Client;
import com.mindhub.homeBanking.repositories.ClientRepository;
import com.mindhub.homeBanking.services.AccountService;
import com.mindhub.homeBanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mindhub.homeBanking.utils.AccountUtils.generateAccountNumber;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        List<Client> clients = clientService.findAllClients();

        Stream<Client> clientStream= clients.stream();
        Stream<ClientDTO> clientDTOStream= clientStream.map(client -> new ClientDTO(client));
        List<ClientDTO> clientDTOS = clientDTOStream.collect(Collectors.toList());
        return clientDTOS;
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<Object> getClient(@PathVariable Long id){

        Client client=clientService.findClientById(id);

        if (client == null) {
            return new ResponseEntity<>("Client not found.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ClientDTO(client), HttpStatus.OK);

    }

    @PostMapping( "/clients")
    public ResponseEntity<Object> register(
            @RequestParam String name, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (name.isEmpty() || name.isBlank()) {
            return new ResponseEntity<>("Missing name or contains whitespace", HttpStatus.FORBIDDEN);
        }
        if (lastName.isEmpty() || lastName.isBlank()) {
            return new ResponseEntity<>("Missing last name or contains whitespace", HttpStatus.FORBIDDEN);
        }
        if (email.isEmpty() || email.isBlank()) {
            return new ResponseEntity<>("Missing email or contains whitespace", HttpStatus.FORBIDDEN);
        }
        if (password.isEmpty() || password.isBlank()) {
            return new ResponseEntity<>("Missing password or contains whitespace", HttpStatus.FORBIDDEN);
        }
        if (clientService.findClientByEmail(email) != null) {
            return new ResponseEntity<>("Username already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(name, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(client);

        Account account= new Account(generateAccountNumber(accountService, 1, 100000000, 8), LocalDate.now(),0.00,true, AccountType.SAVINGS);
        client.addAccount(account);
        accountService.saveAccount(account);


        return new ResponseEntity<>("Client created successfully",HttpStatus.CREATED);
    }

    @GetMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication){
        Client client = clientService.findClientByEmail(authentication.getName());
        ClientDTO clientDTO = Optional.ofNullable(client)
                .map(cli -> new ClientDTO(cli))
                .orElse(null);
        return clientDTO;
    }

}
