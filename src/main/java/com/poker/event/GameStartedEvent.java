package com.poker.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class GameStartedEvent extends ApplicationEvent {
    private final String gameId;
    private final int playerCount;
    private final int startingChips;
    private final int smallBlind;
    private final int bigBlind;
    private final String[] playerIds;
    private final String[] playerNames;

    public GameStartedEvent(Object source, String gameId, int playerCount, int startingChips, 
                          int smallBlind, int bigBlind, String[] playerIds, String[] playerNames) {
        super(source);
        this.gameId = gameId;
        this.playerCount = playerCount;
        this.startingChips = startingChips;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.playerIds = playerIds;
        this.playerNames = playerNames;
    }
}