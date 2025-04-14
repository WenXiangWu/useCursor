package com.poker.event;

import com.poker.model.Game;
import org.springframework.context.ApplicationEvent;

/**
 * 游戏事件基类，所有游戏事件的基类
 */
public abstract class GameEvent extends ApplicationEvent {
    private final Game game;

    public GameEvent(Object source, Game game) {
        super(source);
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
} 