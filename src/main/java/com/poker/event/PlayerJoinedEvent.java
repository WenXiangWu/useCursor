package com.poker.event;

import com.poker.model.Game;
import com.poker.model.Player;

/**
 * 玩家加入事件
 */
public class PlayerJoinedEvent extends GameEvent {
    private final Player player;

    public PlayerJoinedEvent(Object source, Game game, Player player) {
        super(source, game);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}