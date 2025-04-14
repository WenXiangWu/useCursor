package com.poker.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BlindsChangedEvent extends ApplicationEvent {
    private final String gameId;
    private final int oldSmallBlind;
    private final int oldBigBlind;
    private final int newSmallBlind;
    private final int newBigBlind;
    private final int roundNumber;
    private final long changeTime;  // 变更时间戳
    private final String reason;    // 变更原因：TIME_BASED, PLAYER_COUNT_CHANGED, MANUAL

    public BlindsChangedEvent(Object source, String gameId, int oldSmallBlind, int oldBigBlind, 
                            int newSmallBlind, int newBigBlind, int roundNumber, 
                            long changeTime, String reason) {
        super(source);
        this.gameId = gameId;
        this.oldSmallBlind = oldSmallBlind;
        this.oldBigBlind = oldBigBlind;
        this.newSmallBlind = newSmallBlind;
        this.newBigBlind = newBigBlind;
        this.roundNumber = roundNumber;
        this.changeTime = changeTime;
        this.reason = reason;
    }
}