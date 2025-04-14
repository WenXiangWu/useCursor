package com.poker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 玩家类，管理玩家的状态和行为
 */
public class Player {
    private final String id;
    private String name;
    private int chips;
    private final List<Card> cards;
    private int currentBet;
    private boolean folded;
    private boolean allIn;
    private boolean isHost;
    private boolean connected;
    private long lastActionTime;
    private String avatar;
    private String status;
    private int position;
    private int totalWinnings;
    private int handsWon;
    private int handsPlayed;

    public Player(String name, int chips) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.chips = chips;
        this.cards = new ArrayList<>();
        this.currentBet = 0;
        this.folded = false;
        this.allIn = false;
        this.isHost = false;
        this.connected = true;
        this.lastActionTime = System.currentTimeMillis();
        this.avatar = "default.png";
        this.status = "ready";
        this.position = -1;
        this.totalWinnings = 0;
        this.handsWon = 0;
        this.handsPlayed = 0;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void clearCards() {
        cards.clear();
        currentBet = 0;
        folded = false;
        allIn = false;
    }

    public void bet(int amount) {
        if (amount <= chips) {
            chips -= amount;
            currentBet += amount;
            if (chips == 0) {
                allIn = true;
            }
            updateLastActionTime();
        }
    }

    public void fold() {
        folded = true;
        updateLastActionTime();
    }

    public void win(int amount) {
        chips += amount;
        totalWinnings += amount;
        handsWon++;
        updateLastActionTime();
    }

    public void setHost(boolean isHost) {
        this.isHost = isHost;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void updateLastActionTime() {
        this.lastActionTime = System.currentTimeMillis();
    }

    public boolean hasFolded() {
        return folded;
    }

    public boolean isAllIn() {
        return allIn;
    }

    public boolean isHost() {
        return isHost;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isActive() {
        return !folded && !allIn && connected;
    }

    public boolean isTimeout(long timeout) {
        return System.currentTimeMillis() - lastActionTime > timeout;
    }

    public HandRank getHandRank(List<Card> communityCards) {
        List<Card> allCards = new ArrayList<>(cards);
        allCards.addAll(communityCards);
        return HandEvaluator.evaluateHand(allCards);
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public int getChips() { return chips; }
    public List<Card> getCards() { return cards; }
    public int getCurrentBet() { return currentBet; }
    public int getPosition() { return position; }
    public String getAvatar() { return avatar; }
    public String getStatus() { return status; }
    public int getTotalWinnings() { return totalWinnings; }
    public int getHandsWon() { return handsWon; }
    public int getHandsPlayed() { return handsPlayed; }
    public long getLastActionTime() { return lastActionTime; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setChips(int chips) { this.chips = chips; }
}