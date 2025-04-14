package com.poker.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PlayerLeftEvent extends ApplicationEvent {
    private final String gameId;
    private final String playerId;
    private final String playerName;
    private final int remainingPlayers;
    private final String reason;  // 离开原因：DISCONNECTED, TIMEOUT, VOLUNTARY
    private final int remainingChips;  // 剩余筹码

    public PlayerLeftEvent(Object source, String gameId, String playerId, String playerName, 
                         int remainingPlayers, String reason, int remainingChips) {
        super(source);
        this.gameId = gameId;
        this.playerId = playerId;
        this.playerName = playerName;
        this.remainingPlayers = remainingPlayers;
        this.reason = reason;
        this.remainingChips = remainingChips;
    }
}