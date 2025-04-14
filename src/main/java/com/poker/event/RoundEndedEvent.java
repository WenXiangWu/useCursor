package com.poker.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RoundEndedEvent extends ApplicationEvent {
    private final String gameId;
    private final int roundNumber;
    private final String winnerId;
    private final String winnerName;
    private final int potAmount;
    private final String[] communityCards;
    private final String[][] winningHand;  // 获胜玩家的手牌
    private final String handRank;         // 牌型大小
    private final long roundDuration;      // 本轮持续时间（毫秒）

    public RoundEndedEvent(Object source, String gameId, int roundNumber, String winnerId, 
                          String winnerName, int potAmount, String[] communityCards, 
                          String[][] winningHand, String handRank, long roundDuration) {
        super(source);
        this.gameId = gameId;
        this.roundNumber = roundNumber;
        this.winnerId = winnerId;
        this.winnerName = winnerName;
        this.potAmount = potAmount;
        this.communityCards = communityCards;
        this.winningHand = winningHand;
        this.handRank = handRank;
        this.roundDuration = roundDuration;
    }
}