package com.poker.event;

import com.poker.model.Game;
import com.poker.model.Player;
import org.springframework.context.ApplicationEvent;

/**
 * 玩家加入事件
 */
public class PlayerJoinedEvent extends ApplicationEvent {
    private final Game game;
    private final Player player;

    public PlayerJoinedEvent(Object source, Game game, Player player) {
        super(source);
        this.game = game;
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }
}