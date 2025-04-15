package com.poker.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.ToString;
import lombok.AllArgsConstructor;

@Getter
@ToString
@AllArgsConstructor
public class Card implements Serializable, Comparable<Card> {
    private static final long serialVersionUID = 1L;
    
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
        TWO("2", 2),
        THREE("3", 3),
        FOUR("4", 4),
        FIVE("5", 5),
        SIX("6", 6),
        SEVEN("7", 7),
        EIGHT("8", 8),
        NINE("9", 9),
        TEN("10", 10),
        JACK("J", 11),
        QUEEN("Q", 12),
        KING("K", 13),
        ACE("A", 14);

        private final String symbol;
        private final int value;

        Rank(String symbol, int value) {
            this.symbol = symbol;
            this.value = value;
        }

        public String getSymbol() {
            return symbol;
        }

        public int getValue() {
            return value;
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
            suit.getSymbol().toLowerCase());
    }

    public String getImagePath() {
        return imagePath;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
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

    @Override
    public String toString() {
        return rank.getSymbol() + suit.getSymbol();
    }

    @Override
    public int compareTo(Card other) {
        if (this.rank.getValue() != other.rank.getValue()) {
            return Integer.compare(this.rank.getValue(), other.rank.getValue());
        }
        return this.suit.compareTo(other.suit);
    }
}