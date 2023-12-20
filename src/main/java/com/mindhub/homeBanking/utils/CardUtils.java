package com.mindhub.homeBanking.utils;

public final class CardUtils {

    private CardUtils(){

    }

    public static String generateCardNumber() {
        String cardNumber = "";
        for (int i = 0; i < 4; i++) {
            cardNumber += generateFourDigitNumber();
            if (i < 3) {
                cardNumber += "-";
            }
        }
        return cardNumber;
    }

    public static String generateFourDigitNumber() {
        int randomNumber = (int) (Math.random() * 10000);
        return String.format("%04d", randomNumber);
    }

    public static String generateCvvNumber() {
        int randomNumber = (int) (Math.random() * 1000);
        return String.format("%03d", randomNumber);
    }

}
