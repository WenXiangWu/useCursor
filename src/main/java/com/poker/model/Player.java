package com.poker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.Data;
import java.io.Serializable;

/**
 * 玩家类，表示游戏中的一个玩家
 */
@Data
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String username;
    private int chips;
    private boolean connected;
    private PlayerState state;
    private int currentBet;
    private Card[] holeCards;
    private int position;
    private String name;
    private boolean isDealer;
    private boolean isSmallBlind;
    private boolean isBigBlind;
    private boolean isActive;
    private boolean folded;
    private boolean allIn;
    private boolean isHost;
    private long lastActionTime;
    private String avatar;
    private String status;
    private int totalBetsPlaced;
    private int totalWinnings;
    private int handsWon;
    private int handsPlayed;

    public enum PlayerState {
        WAITING,    // 等待游戏开始
        ACTIVE,     // 正在游戏中
        FOLDED,     // 已弃牌
        ALL_IN,     // 全押
        DISCONNECTED // 断开连接
    }

    public Player(String id, String username, int startingChips) {
        this.id = id;
        this.username = username;
        this.chips = startingChips;
        this.connected = true;
        this.state = PlayerState.WAITING;
        this.currentBet = 0;
        this.holeCards = new Card[2];
        this.position = -1;
        this.name = username;
        this.isActive = true;
        this.folded = false;
        this.allIn = false;
        this.isDealer = false;
        this.isSmallBlind = false;
        this.isBigBlind = false;
        this.lastActionTime = System.currentTimeMillis();
        this.avatar = "default.png";
        this.status = "ready";
        this.totalBetsPlaced = 0;
        this.totalWinnings = 0;
        this.handsWon = 0;
        this.handsPlayed = 0;
        resetRoundState();
    }

    public void placeBet(int amount) {
        if (amount > chips) {
            throw new IllegalArgumentException("下注金额不能超过持有筹码数");
        }
        chips -= amount;
        currentBet += amount;
        totalBetsPlaced += amount;
    }

    public void addChips(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("不能添加负数筹码");
        }
        chips += amount;
    }

    public void resetBet() {
        currentBet = 0;
    }

    public void setHoleCards(Card[] cards) {
        if (cards == null || cards.length != 2) {
            throw new IllegalArgumentException("必须提供两张手牌");
        }
        this.holeCards = cards.clone();
    }

    public void resetRoundState() {
        isDealer = false;
        isSmallBlind = false;
        isBigBlind = false;
        folded = false;
        allIn = false;
        currentBet = 0;
        clearHoleCards();
    }

    public void fold() {
        folded = true;
        state = PlayerState.FOLDED;
        clearHoleCards();
    }

    public void allIn() {
        int allInAmount = chips;
        placeBet(allInAmount);
        allIn = true;
        state = PlayerState.ALL_IN;
    }

    public boolean canAct() {
        return isActive && !folded && !allIn && chips > 0;
    }

    public int getRequiredCallAmount(int currentBetAmount) {
        return currentBetAmount - currentBet;
    }

    public boolean hasEnoughChips(int amount) {
        return chips >= amount;
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

    public String getUsername() {
        return username;
    }

    public boolean isActive() {
        return isActive && !folded && !allIn && connected;
    }

    public boolean isTimeout(long timeout) {
        return System.currentTimeMillis() - lastActionTime > timeout;
    }

    public HandEvaluator.HandResult evaluateHand(List<Card> communityCards) {
        List<Card> allCards = new ArrayList<>();
        if (holeCards != null) {
            allCards.addAll(Arrays.asList(holeCards));
        }
        if (communityCards != null) {
            allCards.addAll(communityCards);
        }
        HandEvaluator evaluator = new HandEvaluator();
        return evaluator.evaluate(Arrays.asList(holeCards), communityCards);
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

    @Override
    public String toString() {
        return String.format("Player{id='%s', name='%s', chips=%d, state=%s}", 
            id, name, chips, state);
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public int getChips() { return chips; }
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

    public void bet(int amount) {
        if (amount > chips) {
            amount = chips;
            allIn = true;
        }
        chips -= amount;
        currentBet += amount;
        lastActionTime = System.currentTimeMillis();
    }

    public void resetForNewRound() {
        folded = false;
        allIn = false;
        currentBet = 0;
        clearHoleCards();
    }

    public void clearHoleCards() {
        this.holeCards = new Card[2];
        currentBet = 0;
        folded = false;
        allIn = false;
    }

    public List<Card> getCards() {
        return Arrays.asList(holeCards.clone());
    }
}