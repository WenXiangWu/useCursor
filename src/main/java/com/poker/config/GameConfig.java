package com.poker.config;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameConfig {
    private int smallBlind;
    private int bigBlind;
    private int minPlayers;
    private int maxPlayers;
    private int maxChatHistory;
    private int gameTimeout;
} 