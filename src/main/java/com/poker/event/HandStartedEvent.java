package com.poker.event;

import org.springframework.context.ApplicationEvent;
import java.util.List;

public class HandStartedEvent extends ApplicationEvent {
    private final String gameId;
    private final int handNumber;
    private final List<String> players;
    private final String dealerId;
    private final long startTime;

    public HandStartedEvent(Object source, String gameId, int handNumber, List<String> players, 
                          String dealerId, long startTime) {
        super(source);
        this.gameId = gameId;
        this.handNumber = handNumber;
        this.players = players;
        this.dealerId = dealerId;
        this.startTime = startTime;
    }

    public String getGameId() {
        return gameId;
    }

    public int getHandNumber() {
        return handNumber;
    }

    public List<String> getPlayers() {
        return players;
    }

    public String getDealerId() {
        return dealerId;
    }

    public long getStartTime() {
        return startTime;
    }
} 