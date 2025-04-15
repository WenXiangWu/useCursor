package com.poker.event;

import org.springframework.context.ApplicationEvent;

public class DealerChangedEvent extends ApplicationEvent {
    private final String gameId;
    private final String oldDealerId;
    private final String newDealerId;
    private final int roundNumber;
    private final long changeTime;

    public DealerChangedEvent(Object source, String gameId, String oldDealerId, 
                            String newDealerId, int roundNumber, long changeTime) {
        super(source);
        this.gameId = gameId;
        this.oldDealerId = oldDealerId;
        this.newDealerId = newDealerId;
        this.roundNumber = roundNumber;
        this.changeTime = changeTime;
    }

    public String getGameId() {
        return gameId;
    }

    public String getOldDealerId() {
        return oldDealerId;
    }

    public String getNewDealerId() {
        return newDealerId;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public long getChangeTime() {
        return changeTime;
    }
}