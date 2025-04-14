package com.poker.event;

import com.poker.model.Card;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class CardsDealtEvent extends ApplicationEvent {
    private final String gameId;
    private final String playerId;
    private final List<Card> cards;
    private final int round;

    public CardsDealtEvent(Object source, String gameId, String playerId, List<Card> cards, int round) {
        super(source);
        this.gameId = gameId;
        this.playerId = playerId;
        this.cards = cards;
        this.round = round;
    }

    public String getGameId() {
        return gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getRound() {
        return round;
    }
}