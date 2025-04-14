package com.poker.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CommunityCardDealtEvent extends ApplicationEvent {
    private final String gameId;
    private final String[] cards;
    private final String stage;  // FLOP, TURN, RIVER
    private final int roundNumber;
    private final long dealTime;  // 发牌时间戳
    private final String[] activePlayerIds;  // 当前活跃玩家

    public CommunityCardDealtEvent(Object source, String gameId, String[] cards, String stage, 
                                 int roundNumber, long dealTime, String[] activePlayerIds) {
        super(source);
        this.gameId = gameId;
        this.cards = cards;
        this.stage = stage;
        this.roundNumber = roundNumber;
        this.dealTime = dealTime;
        this.activePlayerIds = activePlayerIds;
    }
}