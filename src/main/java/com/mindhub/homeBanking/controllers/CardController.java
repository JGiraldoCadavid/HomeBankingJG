package com.mindhub.homeBanking.controllers;

import com.mindhub.homeBanking.dtos.CardDTO;
import com.mindhub.homeBanking.dtos.PayWithCardDTO;
import com.mindhub.homeBanking.models.*;
import com.mindhub.homeBanking.services.AccountService;
import com.mindhub.homeBanking.services.CardService;
import com.mindhub.homeBanking.services.ClientService;
import com.mindhub.homeBanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homeBanking.utils.CardUtils.generateCardNumber;
import static com.mindhub.homeBanking.utils.CardUtils.generateCvvNumber;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private CardService cardService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/clients/current/cards")
    public List<CardDTO> getCardsClientCurrent(Authentication authentication) {

        Client client = clientService.findClientByEmail(authentication.getName());

        List<CardDTO> cardDTOList= client.getCards().stream()
                .filter(card -> card.isActive())
                .map(card -> new CardDTO(card))
                .collect(Collectors.toList());

        return cardDTOList;
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardColor color, @RequestParam CardType type,
                                             Authentication authentication){

        Client client = clientService.findClientByEmail(authentication.getName());

        Card existingCard = cardService.findCardByColorAndTypeAndClientCard(color, type, client);

        if (existingCard != null && existingCard.isActive()) {
            return new ResponseEntity<>("You already have an active card of this type and color.", HttpStatus.FORBIDDEN);
        }

        Card card= new Card(client.getName()+" "+client.getLastName(),type,color,generateCardNumber(),generateCvvNumber(),LocalDate.now(), LocalDate.now().plusYears(5), true);
        client.addCard(card);
        cardService.saveCard(card);


        return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);
    }

    @PatchMapping("/clients/current/cards/deactivate")
    public ResponseEntity<Object> deactivateCard(@RequestParam Long id, Authentication authentication){

        Client client = clientService.findClientByEmail(authentication.getName());

        Card card= cardService.findCardById(id);

        if(card==null){
            return new ResponseEntity<>("Card does not exists!", HttpStatus.NOT_FOUND);
        }

        if(!client.getCards().stream().anyMatch(crd -> crd.getNumber().equals(card.getNumber()))){
            return new ResponseEntity<>("The card does not belong to the client", HttpStatus.FORBIDDEN);
        }

        if(!card.isActive()){
            return new ResponseEntity<>("The card is already deactivated.", HttpStatus.BAD_REQUEST);
        }

        card.setActive(false);
        cardService.saveCard(card);

        return new ResponseEntity<>("Card eliminated.", HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @Transactional
    @PostMapping("/cards/pay")
    public ResponseEntity<Object> payWithCard(@RequestBody PayWithCardDTO payWithCardDTO){

        if(payWithCardDTO.getNumberCard().isBlank()){
            return new ResponseEntity<>("Missing card number or contains whitespace", HttpStatus.FORBIDDEN);
        }

        Card card = cardService.findCardByNumber(payWithCardDTO.getNumberCard());

        if (card == null) {
            return new ResponseEntity<>("The card does not exist.", HttpStatus.FORBIDDEN);
        }

        if(!card.getType().equals(CardType.DEBIT)){
            return new ResponseEntity<>("The card must be a debit card", HttpStatus.FORBIDDEN);
        }

        Client client=card.getClientCard();

        if (!payWithCardDTO.getCardHolder().contains(client.getName()) ||
                !payWithCardDTO.getCardHolder().contains(client.getLastName())) {
            return new ResponseEntity<>("The card does not belong to the customer entered in the card holder field.", HttpStatus.FORBIDDEN);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        YearMonth datePayWithCardDTO = YearMonth.parse(payWithCardDTO.getExpiration(), formatter);

        YearMonth dateCard = YearMonth.of(card.getThruDate().getYear(), card.getThruDate().getMonth());

        if (!datePayWithCardDTO.equals(dateCard)) {
            return new ResponseEntity<>("The date provided does not match the date on the card.", HttpStatus.FORBIDDEN);
        }

        Account account = client.getAccounts().stream().filter(Account::isActive).min(Comparator.comparing(Account::getCreationDate)).orElse(null);

        if(account == null){
            return new ResponseEntity<>("The account does not exist.",HttpStatus.FORBIDDEN);
        }

        if(payWithCardDTO.getCvc().isBlank()){
            return new ResponseEntity<>("Missing cvv or contains whitespace", HttpStatus.FORBIDDEN);
        }

        if(payWithCardDTO.getAmount()<=0.0){
            return new ResponseEntity<>("Enter a value greater than zero", HttpStatus.FORBIDDEN);
        }

        if(payWithCardDTO.getDescription().isBlank()){
            return new ResponseEntity<>("Missing description or contains whitespace", HttpStatus.FORBIDDEN);
        }

        if(!card.getCvc().equals(payWithCardDTO.getCvc())){
            return new ResponseEntity<>("The cvc provided does not belong to the associated card", HttpStatus.FORBIDDEN);
        }

        if(card.getThruDate().isBefore(LocalDate.now())){
            return new ResponseEntity<>("The card is expired", HttpStatus.FORBIDDEN);
        }

        if(account.getBalance()< payWithCardDTO.getAmount()){
            return new ResponseEntity<>("The amount cannot exceed the amount in the account", HttpStatus.FORBIDDEN);
        }

        Transaction transaction= new Transaction(-payWithCardDTO.getAmount(), payWithCardDTO.getDescription(), LocalDateTime.now(),TransactionType.DEBIT,account.getBalance()- payWithCardDTO.getAmount(),true);
        account.addTransaction(transaction);
        transactionService.saveTransaction(transaction);
        account.setBalance(account.getBalance()-payWithCardDTO.getAmount());
        accountService.saveAccount(account);

        return new ResponseEntity<>("Pay successful", HttpStatus.OK);
    }

}
