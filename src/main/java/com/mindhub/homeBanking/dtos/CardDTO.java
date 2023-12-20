package com.mindhub.homeBanking.dtos;

import com.mindhub.homeBanking.models.Card;
import com.mindhub.homeBanking.models.CardColor;
import com.mindhub.homeBanking.models.CardType;

import java.time.LocalDate;

public class CardDTO {

    private Long id;
    private String cardHolder;
    private CardType type;
    private CardColor color;
    private String number;
    private String cvc;
    private LocalDate thruDate;
    private LocalDate fromDate;
    private boolean isActive;

    public CardDTO(Card card) {
        id = card.getId();
        cardHolder = card.getCardHolder();
        type = card.getType();
        color = card.getColor();
        number = card.getNumber();
        cvc = card.getCvc();
        thruDate = card.getThruDate();
        fromDate = card.getFromDate();
        isActive = card.isActive();
    }

    public Long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public String getCvc() {
        return cvc;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public boolean isActive() {
        return isActive;
    }
}
