package com.mindhub.homeBanking.services.implement;

import com.mindhub.homeBanking.models.Card;
import com.mindhub.homeBanking.models.CardColor;
import com.mindhub.homeBanking.models.CardType;
import com.mindhub.homeBanking.models.Client;
import com.mindhub.homeBanking.repositories.CardRepository;
import com.mindhub.homeBanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;
    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public boolean existsCardByColorAndTypeAndClientCard(CardColor color, CardType type, Client client) {
        return cardRepository.existsByColorAndTypeAndClientCard(color, type, client);
    }

    @Override
    public Card findCardById(Long id) {
        return cardRepository.findCardById(id);
    }

    @Override
    public boolean existsCardById(Long id) {
        return cardRepository.existsById(id);
    }

    @Override
    public Card findCardByColorAndTypeAndClientCard(CardColor color, CardType type, Client client) {
        return cardRepository.findByColorAndTypeAndClientCard(color, type, client);
    }

    @Override
    public Card findCardByNumber(String number) {
        return cardRepository.findByNumber(number);
    }
}
