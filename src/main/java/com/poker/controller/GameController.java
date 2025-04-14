package com.poker.controller;

import com.poker.model.Table;
import com.poker.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @MessageMapping("/create-table")
    @SendTo("/topic/tables")
    public Table createTable(int smallBlind, int bigBlind) {
        return gameService.createTable(smallBlind, bigBlind);
    }

    @MessageMapping("/join-table")
    @SendTo("/topic/table/{tableId}")
    public Table joinTable(String tableId, String playerId) {
        gameService.joinTable(tableId, playerId);
        return gameService.getTable(tableId);
    }

    @MessageMapping("/leave-table")
    @SendTo("/topic/table/{tableId}")
    public Table leaveTable(String tableId, String playerId) {
        gameService.leaveTable(tableId, playerId);
        return gameService.getTable(tableId);
    }

    @MessageMapping("/place-bet")
    @SendTo("/topic/table/{tableId}")
    public Table placeBet(String tableId, String playerId, int amount) {
        Table table = gameService.getTable(tableId);
        // 实现下注逻辑
        return table;
    }
} 