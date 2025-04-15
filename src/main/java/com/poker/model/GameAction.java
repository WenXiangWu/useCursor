package com.poker.model;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class GameAction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String action;
    private Player player;
    private Object data;
    private long timestamp;

    public GameAction(String action, Player player, Object data) {
        this.action = action;
        this.player = player;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
} 