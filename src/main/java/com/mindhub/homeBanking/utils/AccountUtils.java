package com.mindhub.homeBanking.utils;

import com.mindhub.homeBanking.services.AccountService;

import java.util.Random;

public final class AccountUtils {

    private AccountUtils(){

    }

    public static String generateAccountNumber(AccountService accountService, int min, int max, int numberOfDigits) {
        String formatString = "%0" + numberOfDigits + "d";
        int randomNumber;
        Random random= new Random();
        String accountNumber;

        do {
            randomNumber = random.nextInt(max - min) + min;
            accountNumber = "VIN-" + String.format(formatString, randomNumber);
        } while (accountService.existsByNumber(accountNumber));

        return accountNumber;
    }
}
