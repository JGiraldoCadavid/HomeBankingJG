package com.mindhub.homeBanking.repositories;

import com.mindhub.homeBanking.models.Card;
import com.mindhub.homeBanking.models.CardColor;
import com.mindhub.homeBanking.models.CardType;
import com.mindhub.homeBanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {
    boolean existsByColorAndTypeAndClientCard(CardColor color, CardType type, Client client);

    Card findCardById(Long id);

    boolean existsById(Long id);

    Card findByColorAndTypeAndClientCard(CardColor color, CardType type, Client client);

    Card findByNumber(String number);

}
