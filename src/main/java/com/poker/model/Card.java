package com.poker.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Card {
    public enum Suit {
        HEARTS("♥"),
        DIAMONDS("♦"),
        CLUBS("♣"),
        SPADES("♠");

        private final String symbol;

        Suit(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    public enum Rank {
        TWO(2, "2"),
        THREE(3, "3"),
        FOUR(4, "4"),
        FIVE(5, "5"),
        SIX(6, "6"),
        SEVEN(7, "7"),
        EIGHT(8, "8"),
        NINE(9, "9"),
        TEN(10, "10"),
        JACK(11, "J"),
        QUEEN(12, "Q"),
        KING(13, "K"),
        ACE(14, "A");

        private final int value;
        private final String symbol;

        Rank(int value, String symbol) {
            this.value = value;
            this.symbol = symbol;
        }

        public int getValue() {
            return value;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    private final Suit suit;
    private final Rank rank;
    private final String imagePath;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
        this.imagePath = String.format("/images/cards/_of_.png", 
            rank.getSymbol().toLowerCase(), 
            suit.name().toLowerCase());
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return suit == card.suit && rank == card.rank;
    }

    @Override
    public int hashCode() {
        return 31 * suit.hashCode() + rank.hashCode();
    }
}