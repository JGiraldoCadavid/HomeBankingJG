package com.mindhub.homeBanking.dtos;

public class PayWithCardDTO {

    private String numberCard;
    private String cvc;
    private double amount;
    private String description;
    private String cardHolder;
    private String expiration;

    public PayWithCardDTO() {
    }

    public String getNumberCard() {
        return numberCard;
    }

    public String getCvc() {
        return cvc;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getExpiration() {
        return expiration;
    }

}
