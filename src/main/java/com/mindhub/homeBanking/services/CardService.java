package com.mindhub.homeBanking.services;

import com.mindhub.homeBanking.models.Card;
import com.mindhub.homeBanking.models.CardColor;
import com.mindhub.homeBanking.models.CardType;
import com.mindhub.homeBanking.models.Client;

public interface CardService {
    void saveCard(Card card);

    boolean existsCardByColorAndTypeAndClientCard(CardColor color, CardType type, Client client);

    Card findCardById(Long id);

    boolean existsCardById(Long id);

    Card findCardByColorAndTypeAndClientCard(CardColor color, CardType type, Client client);

    Card findCardByNumber(String number);
}
