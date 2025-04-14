package com.poker.controller;

import com.poker.model.Game;
import com.poker.model.Player;
import com.poker.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    private final GameService gameService;

    public PlayerController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/{playerId}/join")
    public ResponseEntity<Game> joinGame(
            @PathVariable String playerId,
            @RequestBody Map<String, String> request) {
        String gameId = request.get("gameId");
        try {
            Player player = new Player(request.get("name"), Integer.parseInt(request.get("chips")));
            Game game = gameService.joinGame(gameId, player);
            return ResponseEntity.ok(game);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{playerId}/leave")
    public ResponseEntity<Void> leaveGame(@PathVariable String playerId) {
        try {
            gameService.leaveGame(playerId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{playerId}/game")
    public ResponseEntity<Game> getPlayerGame(@PathVariable String playerId) {
        Game game = gameService.getPlayerGame(playerId);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(game);
    }

    @PostMapping("/{playerId}/action")
    public ResponseEntity<Void> playerAction(
            @PathVariable String playerId,
            @RequestBody Map<String, String> action) {
        try {
            gameService.playerAction(
                action.get("gameId"),
                playerId,
                action.get("action"),
                Integer.parseInt(action.get("amount"))
            );
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{playerId}/chat")
    public ResponseEntity<Void> sendChatMessage(
            @PathVariable String playerId,
            @RequestBody Map<String, String> message) {
        try {
            gameService.handleChatMessage(
                message.get("gameId"),
                playerId,
                message.get("message"),
                Boolean.parseBoolean(message.get("isPrivate"))
            );
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}