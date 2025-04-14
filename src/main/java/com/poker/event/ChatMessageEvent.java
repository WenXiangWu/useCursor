package com.poker.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChatMessageEvent extends ApplicationEvent {
    private final String gameId;
    private final String playerId;
    private final String playerName;
    private final String message;
    private final long timestamp;
    private final String messageType;  // 消息类型：TEXT, EMOJI, SYSTEM
    private final String[] mentionedPlayers;  // 被提及的玩家
    private final boolean isPrivate;  // 是否为私聊消息

    public ChatMessageEvent(Object source, String gameId, String playerId, String playerName, 
                          String message, long timestamp, String messageType, 
                          String[] mentionedPlayers, boolean isPrivate) {
        super(source);
        this.gameId = gameId;
        this.playerId = playerId;
        this.playerName = playerName;
        this.message = message;
        this.timestamp = timestamp;
        this.messageType = messageType;
        this.mentionedPlayers = mentionedPlayers;
        this.isPrivate = isPrivate;
    }
}