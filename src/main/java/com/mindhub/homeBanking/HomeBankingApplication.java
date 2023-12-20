package com.mindhub.homeBanking;

import com.mindhub.homeBanking.models.*;
import com.mindhub.homeBanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomeBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeBankingApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(
			ClientRepository clientRepository,
			AccountRepository accountRepository,
			TransactionRepository transactionRepository,
			LoanRepository loanRepository,
			ClientLoanRepository clientLoanRepository,
			CardRepository cardRepository
	){
		return args -> {
			Client melba= new Client("Melba", "Morel", "melba@mindhub.com",
					passwordEncoder.encode("Melba1234"));
			Client jon= new Client("Jonathan", "Giraldo","jonagira@gmail.com",
					passwordEncoder.encode("Jon1234"));
			Client admin= new Client("admin","admin","admin@admin.com",
					passwordEncoder.encode("Admin1234"));
			clientRepository.save(melba);
			clientRepository.save(jon);
			clientRepository.save(admin);

			Account account1Melba= new Account("VIN-001", LocalDate.now(),5000,true, AccountType.SAVINGS);
			melba.addAccount(account1Melba);
			accountRepository.save(account1Melba);
			Account account2Melba= new Account("VIN-002",LocalDate.now().plusDays(1),7500,true,AccountType.SAVINGS);
			melba.addAccount(account2Melba);
			accountRepository.save(account2Melba);

			Account account1Jon= new Account("VIN-003", LocalDate.now().minusMonths(3),459.3,true,AccountType.SAVINGS);
			jon.addAccount(account1Jon);
			accountRepository.save(account1Jon);
			Account account2Jon= new Account("VIN-004",LocalDate.now().minusYears(5),26800.5,true,AccountType.SAVINGS);
			jon.addAccount(account2Jon);
			accountRepository.save(account2Jon);

			Transaction transaction1Melba= new Transaction(10000,
					"Savings", LocalDateTime.now(), TransactionType.CREDIT,15000,true);
			Transaction transaction2Melba = new Transaction(350,
					"Payment for first job as a freelance", LocalDateTime.now(), TransactionType.CREDIT,15350,true);
			Transaction transaction3Melba= new Transaction(-500,
					"Payment Car fee", LocalDateTime.now(), TransactionType.DEBIT,14850,true);
			Transaction transaction4Melba= new Transaction(50,
					"Private class", LocalDateTime.now(), TransactionType.CREDIT,14900,true);
			Transaction transaction5Melba= new Transaction(-125,
					"Video game purchase", LocalDateTime.now(), TransactionType.DEBIT,14775,true);
			account1Melba.addTransaction(transaction1Melba);
			transactionRepository.save(transaction1Melba);
			account1Melba.setBalance(account1Melba.getBalance()+10000);
			accountRepository.save(account1Melba);
			account1Melba.addTransaction(transaction2Melba);
			transactionRepository.save(transaction2Melba);
			account1Melba.setBalance(account1Melba.getBalance()+350);
			accountRepository.save(account1Melba);
			account1Melba.addTransaction(transaction3Melba);
			transactionRepository.save(transaction3Melba);
			account1Melba.setBalance(account1Melba.getBalance()-500);
			accountRepository.save(account1Melba);
			account1Melba.addTransaction(transaction4Melba);
			transactionRepository.save(transaction4Melba);
			account1Melba.setBalance(account1Melba.getBalance()+50);
			accountRepository.save(account1Melba);
			account1Melba.addTransaction(transaction5Melba);
			transactionRepository.save(transaction5Melba);
			account1Melba.setBalance(account1Melba.getBalance()-125);
			accountRepository.save(account1Melba);

			Transaction transaction6Melba= new Transaction(125,"Private class",
					LocalDateTime.now(), TransactionType.CREDIT,7625,true);
			Transaction transaction7Melba = new Transaction(-50,"Market buy",
					LocalDateTime.now(), TransactionType.DEBIT,7575,true);
			Transaction transaction8Melba= new Transaction(500,"Birthday gift",
					LocalDateTime.now(), TransactionType.CREDIT,8075,true);
			account2Melba.addTransaction(transaction6Melba);
			transactionRepository.save(transaction6Melba);
			account2Melba.setBalance(account2Melba.getBalance()+125);
			accountRepository.save(account2Melba);
			account2Melba.addTransaction(transaction7Melba);
			transactionRepository.save(transaction7Melba);
			account2Melba.setBalance(account2Melba.getBalance()-50);
			accountRepository.save(account2Melba);
			account2Melba.addTransaction(transaction8Melba);
			transactionRepository.save(transaction8Melba);
			account2Melba.setBalance(account2Melba.getBalance()+500);
			accountRepository.save(account2Melba);

			Transaction transaction1Jon= new Transaction(100,"Private class",
					LocalDateTime.now(), TransactionType.CREDIT,559.3,true);
			Transaction transaction2Jon = new Transaction(-10,"Market buy",
					LocalDateTime.now(), TransactionType.DEBIT,549.3,true);
			Transaction transaction3Jon= new Transaction(25,"Birthday gift",
					LocalDateTime.now(), TransactionType.CREDIT,574.3,true);
			account1Jon.addTransaction(transaction1Jon);
			transactionRepository.save(transaction1Jon);
			account1Jon.setBalance(account1Jon.getBalance()+100);
			accountRepository.save(account1Jon);
			account1Jon.addTransaction(transaction2Jon);
			transactionRepository.save(transaction2Jon);
			account1Jon.setBalance(account1Jon.getBalance()-10);
			accountRepository.save(account1Jon);
			account1Jon.addTransaction(transaction3Jon);
			transactionRepository.save(transaction3Jon);
			account1Jon.setBalance(account1Jon.getBalance()+25);
			accountRepository.save(account1Jon);

			Transaction transaction4Jon= new Transaction(24,"Private class",
					LocalDateTime.now(), TransactionType.CREDIT,26824.5,true);
			Transaction transaction5Jon = new Transaction(-5,"Market buy",
					LocalDateTime.now(), TransactionType.DEBIT,26819.5,true);
			account2Jon.addTransaction(transaction4Jon);
			transactionRepository.save(transaction4Jon);
			account2Jon.setBalance(account2Jon.getBalance()+24);
			accountRepository.save(account2Jon);
			account2Jon.addTransaction(transaction5Jon);
			transactionRepository.save(transaction5Jon);
			account2Jon.setBalance(account2Jon.getBalance()-5);
			accountRepository.save(account2Jon);

			Loan loan1= new Loan("Mortgage",500000, List.of(12,24,36,48,60), 0.10);
			loanRepository.save(loan1);
			Loan loan2= new Loan("Personal", 100000, List.of(6,12,24), 0.15);
			loanRepository.save(loan2);
			Loan loan3= new Loan("Automotive",300000, List.of(6,12,24,36), 0.20);
			loanRepository.save(loan3);

			ClientLoan clientLoanMelba1=new ClientLoan(400000*1.1,60,melba,loan1, true);
			ClientLoan clientLoanMelba2=new ClientLoan(50000*1.15,12,melba,loan2, true);
			melba.getLoans().add(clientLoanMelba1);
			melba.getLoans().add(clientLoanMelba2);
			loan1.getClients().add(clientLoanMelba1);
			loan2.getClients().add(clientLoanMelba2);
			clientLoanRepository.save(clientLoanMelba1);
			clientLoanRepository.save(clientLoanMelba2);
			Transaction transactionLoan1= new Transaction(400000,"Loan Mortgage",
					LocalDateTime.now(), TransactionType.CREDIT,414775,true);
			Transaction transactionLoan2= new Transaction(50000,"Loan Personal",
					LocalDateTime.now(), TransactionType.CREDIT,58075,true);
			account1Melba.addTransaction(transactionLoan1);
			transactionRepository.save(transactionLoan1);
			account1Melba.setBalance(account1Melba.getBalance()+400000);
			accountRepository.save(account1Melba);
			account2Melba.addTransaction(transactionLoan2);
			transactionRepository.save(transactionLoan2);
			account2Melba.setBalance(account2Melba.getBalance()+50000);
			accountRepository.save(account2Melba);


			ClientLoan clientLoanJon1=new ClientLoan(100000*1.1,24,jon,loan1, true);
			ClientLoan clientLoanJon2=new ClientLoan(200000*1.2,36,jon,loan3, true);
			jon.getLoans().add(clientLoanJon1);
			jon.getLoans().add(clientLoanJon2);
			loan1.getClients().add(clientLoanJon1);
			loan3.getClients().add(clientLoanJon2);
			clientLoanRepository.save(clientLoanJon1);
			clientLoanRepository.save(clientLoanJon2);
			Transaction transactionLoan3= new Transaction(100000,"Loan Mortgage",
					LocalDateTime.now(), TransactionType.CREDIT,100574.30,true);
			Transaction transactionLoan4= new Transaction(200000,"Loan Automotive",
					LocalDateTime.now(), TransactionType.CREDIT,226819.50,true);
			account1Jon.addTransaction(transactionLoan3);
			transactionRepository.save(transactionLoan3);
			account1Jon.setBalance(account1Jon.getBalance()+100000);
			accountRepository.save(account1Jon);
			account2Jon.addTransaction(transactionLoan4);
			transactionRepository.save(transactionLoan4);
			account2Jon.setBalance(account2Jon.getBalance()+200000);
			accountRepository.save(account2Jon);

			Card card1Melba = new Card(melba.getName() +" "+ melba.getLastName(),CardType.DEBIT,
					CardColor.GOLD, "4343-4356-7878-9090","056",LocalDate.now(),
					LocalDate.now().plusYears(5), true);
			melba.addCard(card1Melba);
			cardRepository.save(card1Melba);
			Card card3Melba = new Card(melba.getName() +" "+ melba.getLastName(),CardType.DEBIT,
					CardColor.TITANIUM, "4343-4356-7878-5050","026",LocalDate.now().minusYears(10),
					LocalDate.now().minusYears(5), true);
			melba.addCard(card3Melba);
			cardRepository.save(card3Melba);

			Card card2Melba = new Card(melba.getName() +" "+ melba.getLastName(),CardType.CREDIT,
					CardColor.TITANIUM, "4673-8976-7846-3674","156",LocalDate.now(),
					LocalDate.now().plusYears(5), true);
			melba.addCard(card2Melba);
			cardRepository.save(card2Melba);

			Card card1Jon = new Card(jon.getName() +" "+ jon.getLastName(),CardType.DEBIT,
					CardColor.SILVER, "4673-8976-2342-7423","545",LocalDate.now().minusMonths(8),
					LocalDate.now().plusYears(4), true);
			jon.addCard(card1Jon);
			cardRepository.save(card1Jon);
		};
	}

}
