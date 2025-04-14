package com.poker.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PlayerTimeoutEvent extends ApplicationEvent {
    private final String gameId;
    private final String playerId;
    private final String playerName;
    private final long timeoutDuration;  // 超时时间（毫秒）
    private final String action;         // 默认采取的行动（FOLD）
    private final int remainingTimeouts; // 剩余超时次数
    private final long timestamp;        // 超时发生时间戳
    private final String roundStage;     // 当前轮次阶段

    public PlayerTimeoutEvent(Object source, String gameId, String playerId, String playerName, 
                            long timeoutDuration, String action, int remainingTimeouts, 
                            long timestamp, String roundStage) {
        super(source);
        this.gameId = gameId;
        this.playerId = playerId;
        this.playerName = playerName;
        this.timeoutDuration = timeoutDuration;
        this.action = action;
        this.remainingTimeouts = remainingTimeouts;
        this.timestamp = timestamp;
        this.roundStage = roundStage;
    }
}