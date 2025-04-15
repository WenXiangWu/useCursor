package com.poker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;
    private int currentIndex;

    public Deck() {
        cards = new ArrayList<>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
        currentIndex = 0;
    }

    public Card drawCard() {
        if (currentIndex >= cards.size()) {
            throw new IllegalStateException("No more cards in deck");
        }
        return cards.get(currentIndex++);
    }

    public int remainingCards() {
        return cards.size() - currentIndex;
    }

    public void reset() {
        currentIndex = 0;
        shuffle();
    }
}