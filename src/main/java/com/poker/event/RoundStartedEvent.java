package com.poker.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RoundStartedEvent extends ApplicationEvent {
    private final String gameId;
    private final int roundNumber;
    private final String currentPlayerId;
    private final int smallBlind;
    private final int bigBlind;
    private final String[] activePlayerIds;  // 当前轮次活跃玩家
    private final int[] playerChips;         // 玩家当前筹码数

    public RoundStartedEvent(Object source, String gameId, int roundNumber, String currentPlayerId, 
                           int smallBlind, int bigBlind, String[] activePlayerIds, int[] playerChips) {
        super(source);
        this.gameId = gameId;
        this.roundNumber = roundNumber;
        this.currentPlayerId = currentPlayerId;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.activePlayerIds = activePlayerIds;
        this.playerChips = playerChips;
    }
}