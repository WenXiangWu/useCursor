package com.poker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;
    private int currentIndex;

    public Deck() {
        initializeDeck();
    }

    private void initializeDeck() {
        cards = new ArrayList<>(52);
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        currentIndex = 0;
    }

    public void shuffle() {
        Collections.shuffle(cards);
        currentIndex = 0;
    }

    public Card dealCard() {
        if (currentIndex >= cards.size()) {
            throw new IllegalStateException("No more cards in the deck");
        }
        return cards.get(currentIndex++);
    }

    public List<Card> dealCards(int count) {
        List<Card> dealtCards = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            dealtCards.add(dealCard());
        }
        return dealtCards;
    }

    public void reset() {
        currentIndex = 0;
    }

    public int remainingCards() {
        return cards.size() - currentIndex;
    }

    public boolean isEmpty() {
        return currentIndex >= cards.size();
    }
}