package com.mindhub.homeBanking.controllers;

import com.mindhub.homeBanking.dtos.AccountDTO;
import com.mindhub.homeBanking.models.Account;
import com.mindhub.homeBanking.models.AccountType;
import com.mindhub.homeBanking.models.Client;
import com.mindhub.homeBanking.services.AccountService;
import com.mindhub.homeBanking.services.ClientService;
import com.mindhub.homeBanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homeBanking.utils.AccountUtils.generateAccountNumber;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;


    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {

        return accountService.findAllAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());

    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication) {

        Account account = accountService.findAccountById(id);
        Client client= clientService.findClientByEmail(authentication.getName());

        if (account == null) {
            return new ResponseEntity<>("Account not found.", HttpStatus.NOT_FOUND);
        }

        if(!client.getAccounts().stream().filter(acc -> acc.isActive()).anyMatch(acc -> acc.getNumber().equals(account.getNumber()))){
            return new ResponseEntity<>("The account does not belong to the client", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(new AccountDTO(account), HttpStatus.OK);

    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getAccountsClientCurrent(Authentication authentication) {

        Client client = clientService.findClientByEmail(authentication.getName());

        List<AccountDTO> accountDTOList= client.getAccounts().stream().filter(acc -> acc.isActive()).map(
                account -> new AccountDTO(account)).collect(Collectors.toList());

        return accountDTOList;
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(@RequestParam AccountType type, Authentication authentication){

        Client client = clientService.findClientByEmail(authentication.getName());

        if (type == null || !EnumSet.of(AccountType.SAVINGS, AccountType.CHECKING).contains(type)) {
            return new ResponseEntity<>("To create an account, you need to send a valid account type.",
                    HttpStatus.FORBIDDEN);
        }

        if(client.getAccounts().stream().filter(acc -> acc.isActive()).collect(Collectors.toList()).size()>=3){
            return new ResponseEntity<>("The maximum number of accounts that can be created is 3 per client.", HttpStatus.FORBIDDEN);
        }

        Account account = new Account(generateAccountNumber(accountService, 1, 100000000, 8), LocalDate.now(), 0.00, true, type);
        client.addAccount(account);
        accountService.saveAccount(account);

        return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);

    }

    @PatchMapping("/clients/current/accounts/deactivate")
    public ResponseEntity<Object> deactivateAccount(@RequestParam Long id, Authentication authentication){

        Client client = clientService.findClientByEmail(authentication.getName());

        Account account= accountService.findAccountById(id);

        if(account==null){
            return new ResponseEntity<>("Account does not exists!", HttpStatus.BAD_REQUEST);
        }

        if(!client.getAccounts().stream().anyMatch(acc -> acc.getNumber().equals(account.getNumber()))){
            return new ResponseEntity<>("The account does not belong to the client", HttpStatus.FORBIDDEN);
        }

        if(client.getAccounts().stream().filter(acc -> acc.isActive()).collect(Collectors.toList()).size()==1){
            return new ResponseEntity<>("You cannot delete this account, it is a requirement to have an account to be a customer.", HttpStatus.FORBIDDEN);
        }

        if(account.getBalance()!=0.00){
            return new ResponseEntity<>("In order to delete the account, you must have a balance of 0.00 dollars",HttpStatus.FORBIDDEN);
        }

        if(!account.isActive()){
            return new ResponseEntity<>("The account is already deactivated.", HttpStatus.BAD_REQUEST);
        }

        account.getTransactions().stream().forEach(trns -> {
            trns.setActive(false);
            transactionService.saveTransaction(trns);
        });
        account.setActive(false);
        accountService.saveAccount(account);

        return new ResponseEntity<>("Account eliminated.", HttpStatus.OK);
    }

}
