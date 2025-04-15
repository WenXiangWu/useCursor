package com.poker.service;

import com.poker.model.Game;
import com.poker.model.Player;
import com.poker.config.GameConfig;

public interface GameService {
    Game createGame(GameConfig config);
    void startGame(Game game);
    void joinGame(Game game, Player player);
    void leaveGame(Game game, Player player);
    void handlePlayerAction(Game game, Player player, String action, int amount);
    void handlePlayerTimeout(Game game, Player player);
    void handlePlayerDisconnect(Game game, Player player);
    void handlePlayerReconnect(Game game, Player player);
}