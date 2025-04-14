package com.poker.event;

import com.poker.model.Game;
import com.poker.model.Player;

public class BettingRoundStartedEvent extends GameEvent {
    private final Player firstPlayer;
    private final int roundNumber;
    
    public BettingRoundStartedEvent(Game game, Player firstPlayer, int roundNumber) {
        super(game, game.getId());
        this.firstPlayer = firstPlayer;
        this.roundNumber = roundNumber;
    }
    
    public Player getFirstPlayer() {
        return firstPlayer;
    }
    
    public int getRoundNumber() {
        return roundNumber;
    }
} 