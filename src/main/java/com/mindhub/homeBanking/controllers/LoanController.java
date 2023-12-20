package com.mindhub.homeBanking.controllers;

import com.mindhub.homeBanking.dtos.CreateLoanDTO;
import com.mindhub.homeBanking.dtos.LoanApplicationDTO;
import com.mindhub.homeBanking.dtos.LoanDTO;
import com.mindhub.homeBanking.dtos.PayLoanDTO;
import com.mindhub.homeBanking.models.*;
import com.mindhub.homeBanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping(value = "/loans")
    public List<LoanDTO> getLoans(){

        List<Loan> loans = loanService.findAllLoans();

        List<LoanDTO> loanDTOS = loans.stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());

        return loanDTOS;

    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(@RequestBody LoanApplicationDTO loanApplicationDTO,
                                             Authentication authentication){

        Client client = clientService.findClientByEmail(authentication.getName());
        Loan loan = loanService.findLoanById(loanApplicationDTO.getId());
        Account account = accountService.findAccountByNumber(loanApplicationDTO.getDestinationAccount());

        if (loanApplicationDTO.getAmount() <= 0) {
            return new ResponseEntity<>("The amount cannot be equal to or less than zero", HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getPayments()<=0){
            return new ResponseEntity<>("Payments cannot be equal to or less than zero", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getDestinationAccount().isBlank()) {
            return new ResponseEntity<>("The destination account cannot be empty", HttpStatus.FORBIDDEN);
        }
        if(loan == null){
            return new ResponseEntity<>("The loan does not exist in the database",HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()){
            return new ResponseEntity<>("The loan cannot exceed the maximum amount allowed",HttpStatus.FORBIDDEN);
        }
        if(loan.getPayments().stream().noneMatch(payment -> payment == loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("The requested payment is not a valid value",HttpStatus.FORBIDDEN);
        }
        if(account == null){
            return new ResponseEntity<>("the destination account does not exist in the database",HttpStatus.FORBIDDEN);
        }
        if(client.getAccounts().stream().noneMatch(acc -> acc.getNumber().equals(loanApplicationDTO.getDestinationAccount()))){
            return new ResponseEntity<>("the destination account does not belong to the current client",HttpStatus.FORBIDDEN);
        }
        if (client.getLoans().stream()
                .anyMatch(clientLoan ->
                        clientLoan.isActive() &&
                                clientLoan.getLoan().getId().equals(loanApplicationDTO.getId()))) {
            return new ResponseEntity<>("You already have a loan of this type", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount()*(loan.getInterestRate()+1), loanApplicationDTO.getPayments(), client, loan, true);

        client.getLoans().add(clientLoan);
        loan.getClients().add(clientLoan);
        clientLoanService.saveClientLoan(clientLoan);

        Transaction transaction = new Transaction(loanApplicationDTO.getAmount(), loan.getName()+" loan approved", LocalDateTime.now(),TransactionType.CREDIT, account.getBalance()+ loanApplicationDTO.getAmount(),true);
        account.addTransaction(transaction);
        transactionService.saveTransaction(transaction);

        account.setBalance(account.getBalance()+ loanApplicationDTO.getAmount());
        accountService.saveAccount(account);

        return new ResponseEntity<>("Loan created successfully",HttpStatus.CREATED);
    }

    @PostMapping("/loans/create")
    public ResponseEntity<String> createNewLoan(@RequestBody CreateLoanDTO createLoanDTO, Authentication authentication) {

        String user= authentication.getName();

        if(!user.contains("@admin")){
            return new ResponseEntity<>("You do not have sufficient permissions", HttpStatus.FORBIDDEN);
        }

        if(createLoanDTO.getName().isBlank()){
            return new ResponseEntity<>("The loan name can not be empty.", HttpStatus.FORBIDDEN);
        }

        if(createLoanDTO.getMaxAmount() <= 50000) {
            return new ResponseEntity<>("Maximum loan amount can not be less than or equal to 50000.", HttpStatus.FORBIDDEN);
        }

        if(createLoanDTO.getInterestRate() <= 0) {
            return new ResponseEntity<>("The interest rate cannot be less than or equal to 0.", HttpStatus.FORBIDDEN);
        }

        if(createLoanDTO.getPayments().size() <= 2) {
            return new ResponseEntity<>("At least 3 payment is required to add it.", HttpStatus.FORBIDDEN);
        }

        if(loanService.existsLoanByNameIgnoreCase(createLoanDTO.getName())){
            return new ResponseEntity<>("This loan already exists, enter another.", HttpStatus.FORBIDDEN);
        }

        for (int payment: createLoanDTO.getPayments()) {
            if (payment <= 0) {
                return new ResponseEntity<>("Loan payments can not be less or equal to 0.", HttpStatus.FORBIDDEN);
            }
        }

        Loan loan = new Loan(createLoanDTO.getName(), createLoanDTO.getMaxAmount(), createLoanDTO.getPayments(), createLoanDTO.getInterestRate());
        loanService.saveLoan(loan);

        return new ResponseEntity<>("Loan created successfully",HttpStatus.CREATED);

    }

    @Transactional
    @PostMapping("/loans/pay")
    public ResponseEntity<Object> payLoan(@RequestBody PayLoanDTO payLoanDTO, Authentication authentication){

        System.out.println(payLoanDTO.getClientLoanId());
        System.out.println(payLoanDTO.getAmountPaid());
        System.out.println(payLoanDTO.getPayments());
        System.out.println(payLoanDTO.getNumberAccount());

        Client client = clientService.findClientByEmail(authentication.getName());
        ClientLoan clientLoan = clientLoanService.findClientLoanById(payLoanDTO.getClientLoanId());
        Account account= accountService.findAccountByNumber(payLoanDTO.getNumberAccount());

        System.out.println(clientLoan);

        if(!clientLoanService.existsClientLoanById(payLoanDTO.getClientLoanId())) {
            return new ResponseEntity<>("The loan does not exist.", HttpStatus.NOT_FOUND);
        }

        if(clientLoan.getId()!= payLoanDTO.getClientLoanId()) {
            return new ResponseEntity<>("The loan does not belong to the client", HttpStatus.ACCEPTED);
        }

        if(payLoanDTO.getNumberAccount().isBlank()) {
            return new ResponseEntity<>("The destination account cannot be empty", HttpStatus.FORBIDDEN);
        }

        if(!accountService.existsByNumber(payLoanDTO.getNumberAccount())){
            return new ResponseEntity<>("The account to make the payment does not exist",HttpStatus.FORBIDDEN);
        }

        if(client.getAccounts().stream().noneMatch(acc -> acc.getNumber().equals(payLoanDTO.getNumberAccount()))){
            return new ResponseEntity<>("the destination account does not belong to the current client",HttpStatus.FORBIDDEN);
        }

        if(account.getBalance()< payLoanDTO.getAmountPaid()) {
            return new ResponseEntity<>("The account has no available balance", HttpStatus.FORBIDDEN);
        }

        if(clientLoan.getPayments()< payLoanDTO.getPayments()) {
            return new ResponseEntity<>("The payment amount should not exceed the amount pending payment", HttpStatus.FORBIDDEN);
        }

        if((clientLoan.getAmountPaid()+ payLoanDTO.getAmountPaid())>clientLoan.getAmount()){
            return new ResponseEntity<>("The amount to be paid exceeds the value of the loan", HttpStatus.FORBIDDEN);
        }

        clientLoan.setPaymentsPaid(clientLoan.getPaymentsPaid() + payLoanDTO.getPayments());
        clientLoan.setAmountPaid(clientLoan.getAmountPaid() + payLoanDTO.getAmountPaid());

        Transaction transaction= new Transaction(-payLoanDTO.getAmountPaid(),"loan installment payment",LocalDateTime.now(),TransactionType.DEBIT, account.getBalance() - payLoanDTO.getAmountPaid(), true);

        account.addTransaction(transaction);
        transactionService.saveTransaction(transaction);

        account.setBalance(account.getBalance() - payLoanDTO.getAmountPaid());
        accountService.saveAccount(account);

        if (clientLoan.getPayments()- clientLoan.getPaymentsPaid()==0) {
            clientLoan.setActive(false);
        }

        return new ResponseEntity<>("The payment was successful!", HttpStatus.OK);

    }

}
