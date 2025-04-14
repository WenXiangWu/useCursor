package com.poker.service.impl;

import com.poker.event.*;
import com.poker.model.*;
import com.poker.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 游戏服务实现类
 */
@Service
public class GameServiceImpl implements GameService {

    private final Map<String, Game> games = new ConcurrentHashMap<>();
    private final ApplicationEventPublisher eventPublisher;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GameServiceImpl(ApplicationEventPublisher eventPublisher, SimpMessagingTemplate messagingTemplate) {
        this.eventPublisher = eventPublisher;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public List<Game> getAvailableGames() {
        return games.values().stream()
                .filter(game -> game.getStatus() == GameStatus.WAITING)
                .collect(Collectors.toList());
    }

    @Override
    public Game createGame(GameConfig gameConfig) {
        Game game = new Game(gameConfig);
        games.put(game.getId(), game);
        eventPublisher.publishEvent(new GameCreatedEvent(this, game));
        return game;
    }

    @Override
    public Game getGame(String gameId) {
        return games.get(gameId);
    }

    @Override
    public Game joinGame(String gameId, Player player) {
        Game game = getGame(gameId);
        if (game != null && game.canJoin(player)) {
            game.addPlayer(player);
            eventPublisher.publishEvent(new PlayerJoinedEvent(this, game, player));
            broadcastGameState(game);
            return game;
        }
        return null;
    }

    @Override
    public boolean leaveGame(String gameId, String playerId) {
        Game game = getGame(gameId);
        if (game != null) {
            Player player = game.getPlayer(playerId);
            if (player != null) {
                game.removePlayer(player);
                eventPublisher.publishEvent(new PlayerLeftEvent(this, game, player));
                broadcastGameState(game);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean startGame(String gameId, String playerId) {
        Game game = getGame(gameId);
        if (game != null && game.canStart(playerId)) {
            game.start();
            eventPublisher.publishEvent(new GameStartedEvent(this, game));
            broadcastGameState(game);
            return true;
        }
        return false;
    }

    @Override
    public boolean pauseGame(String gameId, String playerId) {
        Game game = getGame(gameId);
        if (game != null && game.canPause(playerId)) {
            game.pause();
            eventPublisher.publishEvent(new GamePausedEvent(this, game));
            broadcastGameState(game);
            return true;
        }
        return false;
    }

    @Override
    public boolean resumeGame(String gameId, String playerId) {
        Game game = getGame(gameId);
        if (game != null && game.canResume(playerId)) {
            game.resume();
            eventPublisher.publishEvent(new GameResumedEvent(this, game));
            broadcastGameState(game);
            return true;
        }
        return false;
    }

    @Override
    public void handlePlayerAction(String gameId, String playerId, String actionType, Object actionData) {
        Game game = getGame(gameId);
        if (game != null) {
            Player player = game.getPlayer(playerId);
            if (player != null) {
                switch (actionType) {
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
                        if (actionData instanceof Integer) {
                            game.handleRaise(player, (Integer) actionData);
                        }
                        break;
                    case "ALL_IN":
                        game.handleAllIn(player);
                        break;
                }
                eventPublisher.publishEvent(new PlayerActionEvent(this, game, player, actionType, actionData));
                broadcastGameState(game);
            }
        }
    }

    @Override
    public void handleChatMessage(String gameId, String playerId, String content) {
        Game game = getGame(gameId);
        if (game != null) {
            Player player = game.getPlayer(playerId);
            if (player != null) {
                ChatMessage message = new ChatMessage(player, content);
                game.addChatMessage(message);
                eventPublisher.publishEvent(new ChatMessageEvent(this, game, message));
                broadcastChatMessage(game, message);
            }
        }
    }

    @Override
    public Map<String, Object> getGameStatus(String gameId) {
        Game game = getGame(gameId);
        if (game != null) {
            Map<String, Object> status = new HashMap<>();
            status.put("id", game.getId());
            status.put("status", game.getStatus());
            status.put("players", game.getPlayers());
            status.put("currentRound", game.getCurrentRound());
            status.put("pot", game.getPot());
            status.put("currentPlayer", game.getCurrentPlayer());
            status.put("communityCards", game.getCommunityCards());
            status.put("lastAction", game.getLastAction());
            return status;
        }
        return null;
    }

    @Override
    public void handlePlayerTimeout(String gameId, String playerId) {
        Game game = getGame(gameId);
        if (game != null) {
            Player player = game.getPlayer(playerId);
            if (player != null) {
                game.handlePlayerTimeout(player);
                eventPublisher.publishEvent(new PlayerTimeoutEvent(this, game, player));
                broadcastGameState(game);
            }
        }
    }

    @Override
    public void handlePlayerDisconnect(String gameId, String playerId) {
        Game game = getGame(gameId);
        if (game != null) {
            Player player = game.getPlayer(playerId);
            if (player != null) {
                game.handlePlayerDisconnect(player);
                eventPublisher.publishEvent(new PlayerDisconnectedEvent(this, game, player));
                broadcastGameState(game);
            }
        }
    }

    @Override
    public void handlePlayerReconnect(String gameId, String playerId) {
        Game game = getGame(gameId);
        if (game != null) {
            Player player = game.getPlayer(playerId);
            if (player != null) {
                game.handlePlayerReconnect(player);
                eventPublisher.publishEvent(new PlayerReconnectedEvent(this, game, player));
                broadcastGameState(game);
            }
        }
    }

    @Override
    public boolean updateGameConfig(String gameId, GameConfig gameConfig) {
        Game game = getGame(gameId);
        if (game != null && game.canUpdateConfig()) {
            game.updateConfig(gameConfig);
            eventPublisher.publishEvent(new GameConfigUpdatedEvent(this, game));
            broadcastGameState(game);
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> getGameHistory(String gameId) {
        Game game = getGame(gameId);
        if (game != null) {
            return game.getHistory().stream()
                    .map(action -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("type", action.getType());
                        map.put("player", action.getPlayer());
                        map.put("data", action.getData());
                        map.put("timestamp", action.getTimestamp());
                        return map;
                    })
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public void saveGameState(String gameId) {
        Game game = getGame(gameId);
        if (game != null) {
            // TODO: 实现游戏状态持久化
            eventPublisher.publishEvent(new GameStateSavedEvent(this, game));
        }
    }

    @Override
    public boolean loadGameState(String gameId) {
        // TODO: 实现游戏状态加载
        return false;
    }

    @Override
    @Scheduled(fixedRate = 300000) // 每5分钟执行一次
    public void cleanupExpiredGames() {
        long now = System.currentTimeMillis();
        games.entrySet().removeIf(entry -> {
            Game game = entry.getValue();
            return game.isExpired(now);
        });
    }

    private void broadcastGameState(Game game) {
        messagingTemplate.convertAndSend("/topic/game/" + game.getId() + "/state", getGameStatus(game.getId()));
    }

    private void broadcastChatMessage(Game game, ChatMessage message) {
        messagingTemplate.convertAndSend("/topic/game/" + game.getId() + "/chat", message);
    }
} 