package com.poker.model;

public class ChatMessage {
    private String gameId;
    private String playerId;
    private String message;
    private boolean isPrivate;
    private long timestamp;

    public ChatMessage(String gameId, String playerId, String message, boolean isPrivate) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.message = message;
        this.isPrivate = isPrivate;
        this.timestamp = System.currentTimeMillis();
    }

    public String getGameId() { return gameId; }
    public String getPlayerId() { return playerId; }
    public String getMessage() { return message; }
    public boolean isPrivate() { return isPrivate; }
    public long getTimestamp() { return timestamp; }
} 