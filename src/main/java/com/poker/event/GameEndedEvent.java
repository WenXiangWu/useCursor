package com.poker.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class GameEndedEvent extends ApplicationEvent {
    private final String gameId;
    private final String winnerId;
    private final String winnerName;
    private final int potAmount;
    private final int totalRounds;
    private final long gameDuration;  // 游戏持续时间（毫秒）

    public GameEndedEvent(Object source, String gameId, String winnerId, String winnerName, 
                         int potAmount, int totalRounds, long gameDuration) {
        super(source);
        this.gameId = gameId;
        this.winnerId = winnerId;
        this.winnerName = winnerName;
        this.potAmount = potAmount;
        this.totalRounds = totalRounds;
        this.gameDuration = gameDuration;
    }
}