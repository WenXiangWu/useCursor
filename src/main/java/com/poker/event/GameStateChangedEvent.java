package com.poker.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class GameStateChangedEvent extends ApplicationEvent {
    private final String gameId;
    private final String oldState;
    private final String newState;
    private final String reason;
    private final long timestamp;
    private final String triggeredBy;  // 触发状态变更的玩家ID
    private final String[] affectedPlayers;  // 受影响的玩家列表

    public GameStateChangedEvent(Object source, String gameId, String oldState, String newState, 
                               String reason, long timestamp, String triggeredBy, 
                               String[] affectedPlayers) {
        super(source);
        this.gameId = gameId;
        this.oldState = oldState;
        this.newState = newState;
        this.reason = reason;
        this.timestamp = timestamp;
        this.triggeredBy = triggeredBy;
        this.affectedPlayers = affectedPlayers;
    }
}