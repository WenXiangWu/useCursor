package com.poker.event;

import org.springframework.context.ApplicationEvent;

public class GameStateChangedEvent extends ApplicationEvent {
    private final String gameId;
    private final String oldState;
    private final String newState;
    private final long changeTime;

    public GameStateChangedEvent(Object source, String gameId, String oldState, String newState, long changeTime) {
        super(source);
        this.gameId = gameId;
        this.oldState = oldState;
        this.newState = newState;
        this.changeTime = changeTime;
    }

    public String getGameId() {
        return gameId;
    }

    public String getOldState() {
        return oldState;
    }

    public String getNewState() {
        return newState;
    }

    public long getChangeTime() {
        return changeTime;
    }
}