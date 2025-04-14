package com.poker.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PlayerActionEvent extends ApplicationEvent {
    private final String gameId;
    private final String playerId;
    private final String playerName;
    private final String action;  // FOLD, CHECK, CALL, RAISE, ALL_IN
    private final int amount;     // 下注或加注金额
    private final int currentBet; // 当前下注额
    private final int potSize;    // 当前奖池大小
    private final String nextPlayerId;  // 下一个行动玩家
    private final long actionTime;      // 行动时间戳

    public PlayerActionEvent(Object source, String gameId, String playerId, String playerName, 
                           String action, int amount, int currentBet, int potSize, 
                           String nextPlayerId, long actionTime) {
        super(source);
        this.gameId = gameId;
        this.playerId = playerId;
        this.playerName = playerName;
        this.action = action;
        this.amount = amount;
        this.currentBet = currentBet;
        this.potSize = potSize;
        this.nextPlayerId = nextPlayerId;
        this.actionTime = actionTime;
    }
}