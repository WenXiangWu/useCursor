package com.poker.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Table {
    private String id;
    private List<Player> players;
    private List<Card> communityCards;
    private int pot;
    private int currentBet;
    private int dealerPosition;
    private int smallBlind;
    private int bigBlind;
    private GameState gameState;

    public enum GameState {
        WAITING, DEALING, PRE_FLOP, FLOP, TURN, RIVER, SHOWDOWN
    }

    public Table(int smallBlind, int bigBlind) {
        this.id = UUID.randomUUID().toString();
        this.players = new ArrayList<>();
        this.communityCards = new ArrayList<>();
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.gameState = GameState.WAITING;
    }

    public void addPlayer(Player player) {
        if (players.size() < 9) { // 标准德州扑克最多9人
            players.add(player);
        } else {
            throw new IllegalStateException("Table is full");
        }
    }

    public void removePlayer(String playerId) {
        players.removeIf(p -> p.getId().equals(playerId));
    }

    public void addCommunityCard(Card card) {
        if (communityCards.size() < 5) {
            communityCards.add(card);
        } else {
            throw new IllegalStateException("Cannot add more community cards");
        }
    }

    public void clearTable() {
        communityCards.clear();
        pot = 0;
        currentBet = 0;
        players.forEach(Player::clearCards);
    }
}