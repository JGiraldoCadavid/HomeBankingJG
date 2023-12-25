package com.mindhub.homeBanking.controllers;

import com.lowagie.text.DocumentException;
import com.mindhub.homeBanking.dtos.AccountDTO;
import com.mindhub.homeBanking.models.*;
import com.mindhub.homeBanking.services.AccountService;
import com.mindhub.homeBanking.services.ClientService;
import com.mindhub.homeBanking.services.TransactionService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {


    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(@RequestParam double amount,
                                                    @RequestParam String description,
                                                    @RequestParam String sourceAccountNumber,
                                                    @RequestParam String destinationAccountNumber,
                                                    Authentication authentication){

        Client client = clientService.findClientByEmail(authentication.getName());

        Account sourceAccount = accountService.findAccountByNumber(sourceAccountNumber);
        Account destinationAccount = accountService.findAccountByNumber(destinationAccountNumber);

        if (amount <= 0.0) {
            return new ResponseEntity<>("Enter a value greater than zero", HttpStatus.FORBIDDEN);
        }
        if (description == null || description.isBlank()) {
            return new ResponseEntity<>("Missing description or contains whitespace",
                    HttpStatus.FORBIDDEN);
        }
        if (sourceAccountNumber == null || sourceAccountNumber.isBlank()) {
            return new ResponseEntity<>("Missing source account number or contains whitespace",
                    HttpStatus.FORBIDDEN);
        }
        if (destinationAccountNumber == null || destinationAccountNumber.isBlank()) {
            return new ResponseEntity<>("Missing destination account number or contains whitespace",
                    HttpStatus.FORBIDDEN);
        }
        if(sourceAccountNumber.equals(destinationAccountNumber)){
            return new ResponseEntity<>("Account numbers are the same.", HttpStatus.FORBIDDEN);
        }

        if(sourceAccount==null){
            return new ResponseEntity<>("Account does not exist.", HttpStatus.FORBIDDEN);
        }

        boolean accountExists = client.getAccounts().stream()
                .anyMatch(account -> account.getNumber().equalsIgnoreCase(sourceAccountNumber));

        if(!accountExists){
            return new ResponseEntity<>("The client does not have an account with that number.",
                    HttpStatus.FORBIDDEN);
        }

        if(destinationAccount==null){
            return new ResponseEntity<>("Destination account does not exist.",
                    HttpStatus.FORBIDDEN);
        }

        if(sourceAccount.getBalance()<amount){
            return new ResponseEntity<>("The account has no available balance",
                    HttpStatus.FORBIDDEN);
        }

        Transaction transactionDebit = new Transaction(-amount,description+" "
                +sourceAccountNumber, LocalDateTime.now(), TransactionType.DEBIT, sourceAccount.getBalance()-amount,true);
        Transaction transactionCredit = new Transaction(amount,description+" "
                +destinationAccountNumber, LocalDateTime.now(), TransactionType.CREDIT, destinationAccount.getBalance()+amount,true);

        sourceAccount.addTransaction(transactionDebit);
        destinationAccount.addTransaction(transactionCredit);

        transactionService.saveTransaction(transactionDebit);
        transactionService.saveTransaction(transactionCredit);

        sourceAccount.setBalance(sourceAccount.getBalance()-amount);
        destinationAccount.setBalance(destinationAccount.getBalance()+amount);

        accountService.saveAccount(sourceAccount);
        accountService.saveAccount(destinationAccount);

        AccountDTO accountDTO= new AccountDTO(sourceAccount);

        return new ResponseEntity<>(accountDTO.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/transactions/exportPdf")
    public void exportPDF(@RequestParam Long id, @RequestParam String dateFrom, @RequestParam String dateUntil, HttpServletResponse response) throws DocumentException, IOException {

        LocalDateTime dateF = LocalDateTime.parse(dateFrom);
        LocalDateTime dateU = LocalDateTime.parse(dateUntil);
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey,headerValue);

        List<Transaction> transactionList = transactionService.findByAccount_idAndDateBetweenOrderByDateDesc(id,dateF, dateU);

        PDFExporter exporter= new PDFExporter(transactionList);
        exporter.export(response);

    }

}
