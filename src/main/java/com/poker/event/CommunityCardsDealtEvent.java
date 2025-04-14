package com.poker.event;

import com.poker.model.Card;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class CommunityCardsDealtEvent extends ApplicationEvent {
    private final String gameId;
    private final List<Card> cards;
    private final int round;

    public CommunityCardsDealtEvent(Object source, String gameId, List<Card> cards, int round) {
        super(source);
        this.gameId = gameId;
        this.cards = cards;
        this.round = round;
    }

    public String getGameId() {
        return gameId;
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getRound() {
        return round;
    }
} 