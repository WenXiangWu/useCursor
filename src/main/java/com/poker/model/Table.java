package com.poker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Table {
    private String id;
    private String name;
    private int smallBlind;
    private int bigBlind;
    private List<String> players;
    private String status;

    public Table(String name, int smallBlind, int bigBlind) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.players = new ArrayList<>();
        this.status = "等待中";
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getSmallBlind() { return smallBlind; }
    public int getBigBlind() { return bigBlind; }
    public List<String> getPlayers() { return players; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getPlayerCount() { return players.size(); }

    public boolean addPlayer(String playerId) {
        if (!players.contains(playerId)) {
            return players.add(playerId);
        }
        return false;
    }

    public boolean removePlayer(String playerId) {
        return players.remove(playerId);
    }
}