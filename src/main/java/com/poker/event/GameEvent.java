package com.poker.event;

import com.poker.model.Game;
import com.poker.model.Player;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 游戏事件基类，所有游戏事件的基类
 */
@Getter
public class GameEvent extends ApplicationEvent {
    private static final long serialVersionUID = 1L;

    private final Game game;
    private final Player player;
    private final GameEventType eventType;
    private final String action;
    private final long eventTime;

    public GameEvent(Object source, Game game, GameEventType eventType) {
        this(source, game, null, eventType, null);
    }

    public GameEvent(Object source, Game game, Player player, GameEventType eventType, String action) {
        super(source);
        this.game = game;
        this.player = player;
        this.eventType = eventType;
        this.action = action;
        this.eventTime = System.currentTimeMillis();
    }

    public Game getGame() {
        return game;
    }

    public GameEventType getType() {
        return eventType;
    }

    public long getEventTime() {
        return eventTime;
    }

    public enum GameEventType {
        CREATED,
        STARTED,
        PLAYER_JOINED,
        PLAYER_LEFT,
        PLAYER_ACTION,
        ROUND_ENDED,
        GAME_ENDED,
        PLAYER_TIMEOUT,
        PLAYER_DISCONNECTED,
        PLAYER_RECONNECTED,
        CARDS_DEALT,
        PLAYER_FOLDED,
        CONFIG_UPDATED
    }
}