package com.poker.controller;

import com.poker.model.Game;
import com.poker.model.Player;
import com.poker.service.IGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    private final IGameService gameService;

    public PlayerController(IGameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/{playerId}/join")
    public ResponseEntity<Void> joinGame(
            @PathVariable String playerId,
            @RequestBody Map<String, String> request) {
        String gameId = request.get("gameId");
        try {
            Player player = gameService.getPlayer(playerId);
            if (player == null) {
                player = gameService.createPlayer(request.get("name"), Integer.parseInt(request.get("chips")));
            }
            gameService.joinGame(gameId, player);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{playerId}/leave")
    public ResponseEntity<Void> leaveGame(
            @PathVariable String playerId,
            @RequestBody Map<String, String> request) {
        try {
            Player player = gameService.getPlayer(playerId);
            if (player != null) {
                gameService.leaveGame(request.get("gameId"), player);
            }
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{playerId}/game")
    public ResponseEntity<Game> getPlayerGame(@PathVariable String playerId) {
        Game game = gameService.getCurrentGame();
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
            Player player = gameService.getPlayer(playerId);
            if (player != null) {
                gameService.handlePlayerAction(
                    action.get("gameId"),
                    player,
                    action.get("action"),
                    Integer.parseInt(action.get("amount"))
                );
            }
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}