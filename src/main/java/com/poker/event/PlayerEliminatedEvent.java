package com.poker.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PlayerEliminatedEvent extends ApplicationEvent {
    private final String gameId;
    private final String playerId;
    private final String playerName;
    private final int finalPosition;  // 最终排名
    private final int remainingPlayers;
    private final int totalPlayTime;  // 总游戏时间（毫秒）
    private final int finalChips;     // 最终筹码数
    private final String eliminationReason;  // 淘汰原因：ALL_IN_LOST, TIMEOUT, DISCONNECTED

    public PlayerEliminatedEvent(Object source, String gameId, String playerId, String playerName, 
                                int finalPosition, int remainingPlayers, int totalPlayTime, 
                                int finalChips, String eliminationReason) {
        super(source);
        this.gameId = gameId;
        this.playerId = playerId;
        this.playerName = playerName;
        this.finalPosition = finalPosition;
        this.remainingPlayers = remainingPlayers;
        this.totalPlayTime = totalPlayTime;
        this.finalChips = finalChips;
        this.eliminationReason = eliminationReason;
    }
}