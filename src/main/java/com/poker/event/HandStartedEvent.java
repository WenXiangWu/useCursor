package com.poker.event;

import org.springframework.context.ApplicationEvent;

public class HandStartedEvent extends ApplicationEvent {
    private final String gameId;
    private final String handId;
    private final int round;
    private final int smallBlind;
    private final int bigBlind;
    private final String dealerId;
    private final String smallBlindPlayerId;
    private final String bigBlindPlayerId;

    public HandStartedEvent(Object source, String gameId, String handId, int round,
                          int smallBlind, int bigBlind, String dealerId,
                          String smallBlindPlayerId, String bigBlindPlayerId) {
        super(source);
        this.gameId = gameId;
        this.handId = handId;
        this.round = round;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.dealerId = dealerId;
        this.smallBlindPlayerId = smallBlindPlayerId;
        this.bigBlindPlayerId = bigBlindPlayerId;
    }

    public String getGameId() {
        return gameId;
    }

    public String getHandId() {
        return handId;
    }

    public int getRound() {
        return round;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public String getDealerId() {
        return dealerId;
    }

    public String getSmallBlindPlayerId() {
        return smallBlindPlayerId;
    }

    public String getBigBlindPlayerId() {
        return bigBlindPlayerId;
    }
} 