package com.poker.controller;

import com.poker.model.Game;
import com.poker.model.GameConfig;
import com.poker.model.Player;
import com.poker.service.IGameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@Slf4j
public class GameController {

    private final IGameService gameService;

    @Autowired
    public GameController(IGameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/create")
    public ResponseEntity<Game> createGame(@RequestBody GameConfig config) {
        log.info("收到创建游戏请求，配置: {}", config);
        Game game = gameService.createGame(config);
        log.info("游戏创建成功，返回游戏信息: {}", game);
        return ResponseEntity.ok(game);
    }

    @MessageMapping("/game.create")
    @SendTo("/topic/games")
    public Game handleGameCreate(GameConfig config) {
        log.info("收到WebSocket创建游戏请求");
        return gameService.createGame(config);
    }

    @PostMapping("/{gameId}/join")
    public ResponseEntity<Void> joinGame(@PathVariable String gameId, @RequestBody Player player) {
        log.info("玩家 {} 请求加入游戏 {}", player.getUsername(), gameId);
        gameService.joinGame(gameId, player);
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/game.join")
    public void handleGameJoin(JoinGameRequest request) {
        log.info("收到WebSocket加入游戏请求: {}", request);
        gameService.joinGame(request.getGameId(), request.getPlayer());
    }

    @PostMapping("/{gameId}/start")
    public ResponseEntity<Void> startGame(@PathVariable String gameId, @RequestParam String playerId) {
        log.info("玩家 {} 请求开始游戏 {}", playerId, gameId);
        gameService.startGame(gameId, playerId);
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/game.start")
    public void handleGameStart(StartGameRequest request) {
        log.info("收到WebSocket开始游戏请求: {}", request);
        gameService.startGame(request.getGameId(), request.getPlayerId());
    }

    // 内部请求类
    private static class JoinGameRequest {
        private String gameId;
        private Player player;

        public String getGameId() { return gameId; }
        public Player getPlayer() { return player; }
    }

    private static class StartGameRequest {
        private String gameId;
        private String playerId;

        public String getGameId() { return gameId; }
        public String getPlayerId() { return playerId; }
    }
} 