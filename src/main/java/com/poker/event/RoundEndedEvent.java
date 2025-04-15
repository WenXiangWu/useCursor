package com.poker.event;

import com.poker.model.Game;
import lombok.Getter;

@Getter
public class RoundEndedEvent extends GameEvent {
    private final String gameId;
    private final int roundNumber;
    private final String winnerId;
    private final int potAmount;

    public RoundEndedEvent(Object source, Game game, int roundNumber, String winnerId, int potAmount) {
        super(source, game, null, GameEventType.ROUND_ENDED, "ROUND_ENDED");
        this.gameId = game.getId();
        this.roundNumber = roundNumber;
        this.winnerId = winnerId;
        this.potAmount = potAmount;
    }
}