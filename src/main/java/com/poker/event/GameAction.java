package com.poker.event;

import com.poker.model.Player;

/**
 * 游戏动作类，记录游戏中的各种动作
 */
public class GameAction {
    private final String type;
    private final Player player;
    private final Object data;
    private final long timestamp;

    public GameAction(String type, Player player, Object data, long timestamp) {
        this.type = type;
        this.player = player;
        this.data = data;
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public Player getPlayer() {
        return player;
    }

    public Object getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }
}