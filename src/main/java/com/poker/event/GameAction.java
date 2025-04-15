package com.poker.event;

import com.poker.model.Player;
import lombok.Getter;

@Getter
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
}