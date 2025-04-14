package com.poker.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BetPlacedEvent extends ApplicationEvent {
    private final String gameId;
    private final String playerId;
    private final String playerName;
    private final int betAmount;      // 本次下注金额
    private final int totalBet;       // 玩家在当前轮次的总下注额
    private final int currentBet;     // 当前轮次的最大下注额
    private final int potSize;        // 当前奖池大小
    private final String betType;     // 下注类型：SMALL_BLIND, BIG_BLIND, CALL, RAISE, ALL_IN

    public BetPlacedEvent(Object source, String gameId, String playerId, String playerName, 
                         int betAmount, int totalBet, int currentBet, int potSize, String betType) {
        super(source);
        this.gameId = gameId;
        this.playerId = playerId;
        this.playerName = playerName;
        this.betAmount = betAmount;
        this.totalBet = totalBet;
        this.currentBet = currentBet;
        this.potSize = potSize;
        this.betType = betType;
    }
}