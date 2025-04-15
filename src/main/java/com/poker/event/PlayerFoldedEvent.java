package com.poker.event;

import com.poker.model.Game;
import com.poker.model.Player;
import lombok.Getter;

@Getter
public class PlayerFoldedEvent extends GameEvent {
    private final String gameId;
    private final Player player;

    public PlayerFoldedEvent(Object source, Game game, Player player) {
        super(source, game, player, GameEventType.PLAYER_FOLDED, "FOLD");
        this.gameId = game.getId();
        this.player = player;
    }

    @Override
    public Game getGame() {
        return super.getGame();
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    public String getGameId() {
        return gameId;
    }
} 