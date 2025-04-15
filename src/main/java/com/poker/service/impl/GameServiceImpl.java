package com.poker.service.impl;

import com.poker.event.GameEvent;
import com.poker.model.Game;
import com.poker.model.Player;
import com.poker.model.GameConfig;
import com.poker.service.IGameService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class GameServiceImpl implements IGameService {
    private final Map<String, Game> games = new ConcurrentHashMap<>();
    private final Map<String, Player> players = new ConcurrentHashMap<>();
    private final ApplicationEventPublisher eventPublisher;
    private Game currentGame;

    public GameServiceImpl(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Game getCurrentGame() {
        return currentGame;
    }

    @Override
    public Game createGame(GameConfig config) {
        Game game = new Game(config);
        games.put(game.getId(), game);
        currentGame = game;
        eventPublisher.publishEvent(new GameEvent(this, game, GameEvent.GameEventType.CREATED));
        return game;
    }

    @Override
    public void joinGame(String gameId, Player player) {
        Game game = games.get(gameId);
        if (game != null && game.canJoin(player)) {
            game.addPlayer(player);
            eventPublisher.publishEvent(new GameEvent(this, game, player, GameEvent.GameEventType.PLAYER_JOINED, null));
        }
    }

    @Override
    public void leaveGame(String gameId, Player player) {
        Game game = games.get(gameId);
        if (game != null) {
            game.removePlayer(player);
            eventPublisher.publishEvent(new GameEvent(this, game, player, GameEvent.GameEventType.PLAYER_LEFT, null));
        }
    }

    @Override
    public void handlePlayerAction(String gameId, Player player, String action, Integer amount) {
        Game game = games.get(gameId);
        if (game != null) {
            switch (action.toUpperCase()) {
                case "FOLD":
                    game.handleFold(player);
                    break;
                case "CHECK":
                    game.handleCheck(player);
                    break;
                case "CALL":
                    game.handleCall(player);
                    break;
                case "RAISE":
                    if (amount != null) {
                        game.handleRaise(player, amount);
                    }
                    break;
                case "ALL_IN":
                    game.handleAllIn(player);
                    break;
            }
            eventPublisher.publishEvent(new GameEvent(this, game, player, GameEvent.GameEventType.PLAYER_ACTION, action));
        }
    }

    @Override
    public Player getPlayer(String playerId) {
        return players.get(playerId);
    }

    @Override
    public Player createPlayer(String username, int startingChips) {
        String id = UUID.randomUUID().toString();
        Player player = new Player(id, username, startingChips);
        players.put(id, player);
        return player;
    }

    @Override
    public void startGame(String gameId, String playerId) {
        Game game = games.get(gameId);
        if (game != null && game.canStart(playerId)) {
            game.start();
            eventPublisher.publishEvent(new GameEvent(this, game, GameEvent.GameEventType.STARTED));
        }
    }

    @Override
    public void updateBlinds(String gameId, int smallBlind, int bigBlind) {
        Game game = games.get(gameId);
        if (game != null) {
            GameConfig newConfig = new GameConfig(
                smallBlind,
                bigBlind,
                game.getConfig().getMinPlayers(),
                game.getConfig().getMaxPlayers(),
                game.getConfig().getMaxChatHistory(),
                game.getConfig().getGameTimeout()
            );
            game.updateConfig(newConfig);
            eventPublisher.publishEvent(new GameEvent(this, game, GameEvent.GameEventType.CONFIG_UPDATED));
        }
    }

    @Override
    public void handlePlayerTimeout(String gameId, String playerId) {
        Game game = games.get(gameId);
        if (game != null) {
            Player player = game.getPlayer(playerId);
            if (player != null) {
                game.handlePlayerTimeout(player);
                eventPublisher.publishEvent(new GameEvent(this, game, player, GameEvent.GameEventType.PLAYER_TIMEOUT, null));
            }
        }
    }

    @Override
    public void handlePlayerDisconnect(String gameId, String playerId) {
        Game game = games.get(gameId);
        if (game != null) {
            Player player = game.getPlayer(playerId);
            if (player != null) {
                game.handlePlayerDisconnect(player);
                eventPublisher.publishEvent(new GameEvent(this, game, player, GameEvent.GameEventType.PLAYER_DISCONNECTED, null));
            }
        }
    }

    @Override
    public void handlePlayerReconnect(String gameId, String playerId) {
        Game game = games.get(gameId);
        if (game != null) {
            Player player = game.getPlayer(playerId);
            if (player != null) {
                game.handlePlayerReconnect(player);
                eventPublisher.publishEvent(new GameEvent(this, game, player, GameEvent.GameEventType.PLAYER_RECONNECTED, null));
            }
        }
    }

    @Override
    public Collection<Game> getAllGames() {
        return games.values();
    }
} 