package com.poker.event;

import com.poker.model.Game;
import org.springframework.context.ApplicationEvent;

public class BettingRoundStartedEvent extends ApplicationEvent {
    private final Game game;
    private final String round;

    public BettingRoundStartedEvent(Object source, Game game, String round) {
        super(source);
        this.game = game;
        this.round = round;
    }

    public Game getGame() {
        return game;
    }

    public String getRound() {
        return round;
    }
} 