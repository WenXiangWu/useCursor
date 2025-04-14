package com.poker.event;

import com.poker.game.Game;
import com.poker.model.Player;

public class PlayerFoldedEvent extends GameEvent {
    private final Player player;
    
    public PlayerFoldedEvent(Game game, Player player) {
        super(game);
        this.player = player;
    }
    
    public Player getPlayer() {
        return player;
    }
} 