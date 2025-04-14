package com.poker.controller;

import com.poker.event.GameEvents;
import com.poker.model.Game;
import com.poker.model.GameConfig;
import com.poker.model.Player;
import com.poker.service.GameService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class GameWebSocketController {
    private final GameService gameService;
    private final SimpMessagingTemplate messagingTemplate;
    private final GameEvents gameEvents;

    public GameWebSocketController(GameService gameService, 
                                 SimpMessagingTemplate messagingTemplate,
                                 GameEvents gameEvents) {
        this.gameService = gameService;
        this.messagingTemplate = messagingTemplate;
        this.gameEvents = gameEvents;
    }

    @MessageMapping("/game.create")
    public void createGame(@Payload GameConfig config, SimpMessageHeaderAccessor headerAccessor) {
        Game game = gameService.createGame(config);
        String sessionId = headerAccessor.getSessionId();
        messagingTemplate.convertAndSend("/topic/games", game);
    }

    @MessageMapping("/game.join")
    public void joinGame(@Payload Map<String, String> payload, SimpMessageHeaderAccessor headerAccessor) {
        String gameId = payload.get("gameId");
        String playerName = payload.get("playerName");
        int chips = Integer.parseInt(payload.get("chips"));
        
        Player player = new Player(playerName, chips);
        Game game = gameService.joinGame(gameId, player);
        
        // 将玩家ID与WebSocket会话关联
        headerAccessor.getSessionAttributes().put("PLAYER_ID", player.getId());
        headerAccessor.getSessionAttributes().put("GAME_ID", gameId);
        
        // 通知所有玩家有新玩家加入
        messagingTemplate.convertAndSend("/topic/game." + gameId, 
                Map.of("type", "PLAYER_JOINED", "player", player));
    }

    @MessageMapping("/game.leave")
    public void leaveGame(SimpMessageHeaderAccessor headerAccessor) {
        String playerId = (String) headerAccessor.getSessionAttributes().get("PLAYER_ID");
        String gameId = (String) headerAccessor.getSessionAttributes().get("GAME_ID");
        
        if (playerId != null && gameId != null) {
            gameService.leaveGame(playerId);
            messagingTemplate.convertAndSend("/topic/game." + gameId,
                    Map.of("type", "PLAYER_LEFT", "playerId", playerId));
        }
    }

    @MessageMapping("/game.start")
    public void startGame(@Payload String gameId) {
        gameService.startGame(gameId);
        messagingTemplate.convertAndSend("/topic/game." + gameId,
                Map.of("type", "GAME_STARTED"));
    }

    @MessageMapping("/game.action")
    public void playerAction(@Payload Map<String, String> payload) {
        String gameId = payload.get("gameId");
        String playerId = payload.get("playerId");
        String action = payload.get("action");
        int amount = Integer.parseInt(payload.get("amount"));
        
        gameService.playerAction(gameId, playerId, action, amount);
        messagingTemplate.convertAndSend("/topic/game." + gameId,
                Map.of("type", "PLAYER_ACTION", 
                      "playerId", playerId,
                      "action", action,
                      "amount", amount));
    }

    @MessageMapping("/game.chat")
    public void chatMessage(@Payload Map<String, String> payload) {
        String gameId = payload.get("gameId");
        String playerId = payload.get("playerId");
        String message = payload.get("message");
        boolean isPrivate = Boolean.parseBoolean(payload.get("isPrivate"));
        
        gameService.handleChatMessage(gameId, playerId, message, isPrivate);
        
        if (isPrivate) {
            String targetPlayerId = payload.get("targetPlayerId");
            messagingTemplate.convertAndSendToUser(targetPlayerId, "/queue/chat",
                    Map.of("type", "CHAT_MESSAGE",
                          "playerId", playerId,
                          "message", message,
                          "isPrivate", true));
        } else {
            messagingTemplate.convertAndSend("/topic/game." + gameId + ".chat",
                    Map.of("type", "CHAT_MESSAGE",
                          "playerId", playerId,
                          "message", message,
                          "isPrivate", false));
        }
    }

    @MessageMapping("/game.blinds")
    public void updateBlinds(@Payload Map<String, String> payload) {
        String gameId = payload.get("gameId");
        int smallBlind = Integer.parseInt(payload.get("smallBlind"));
        int bigBlind = Integer.parseInt(payload.get("bigBlind"));
        
        gameService.updateBlinds(gameId, smallBlind, bigBlind);
        messagingTemplate.convertAndSend("/topic/game." + gameId,
                Map.of("type", "BLINDS_CHANGED",
                      "smallBlind", smallBlind,
                      "bigBlind", bigBlind));
    }

    @MessageMapping("/game.pause")
    public void pauseGame(@Payload String gameId) {
        gameService.pauseGame(gameId);
        messagingTemplate.convertAndSend("/topic/game." + gameId,
                Map.of("type", "GAME_PAUSED"));
    }

    @MessageMapping("/game.resume")
    public void resumeGame(@Payload String gameId) {
        gameService.resumeGame(gameId);
        messagingTemplate.convertAndSend("/topic/game." + gameId,
                Map.of("type", "GAME_RESUMED"));
    }
}