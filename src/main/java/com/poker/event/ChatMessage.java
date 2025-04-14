package com.poker.event;

import com.poker.model.Player;

/**
 * 聊天消息类，处理游戏中的聊天消息
 */
public class ChatMessage {
    private final Player player;
    private final String content;
    private final long timestamp;

    public ChatMessage(Player player, String content) {
        this.player = player;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    public Player getPlayer() {
        return player;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }
} 