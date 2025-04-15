package com.poker.controller;

import com.poker.model.Game;
import com.poker.model.GameConfig;
import com.poker.service.IGameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class TableController {

    private static final Logger log = LoggerFactory.getLogger(TableController.class);
    private final IGameService gameService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public TableController(IGameService gameService, SimpMessagingTemplate messagingTemplate) {
        this.gameService = gameService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/table/create")
    @SendTo("/topic/tables")
    public Game createTable(@RequestBody CreateTableRequest request) {
        log.info("收到创建游戏桌请求: {}", request);
        GameConfig config = new GameConfig(
            request.getSmallBlind(),
            request.getBigBlind(),
            2,  // 最小玩家数
            9,  // 最大玩家数
            100,  // 最大聊天历史
            300   // 游戏超时时间
        );
        Game game = gameService.createGame(config);
        log.info("游戏桌创建成功: {}", game);
        
        // 广播新创建的游戏桌
        messagingTemplate.convertAndSend("/topic/tables", game);
        return game;
    }

    @GetMapping("/api/tables")
    public java.util.Collection<Game> getAllTables() {
        log.info("获取所有游戏桌列表");
        return gameService.getAllGames();
    }

    // 请求对象
    public static class CreateTableRequest {
        private int smallBlind;
        private int bigBlind;

        public int getSmallBlind() { return smallBlind; }
        public void setSmallBlind(int smallBlind) { this.smallBlind = smallBlind; }
        public int getBigBlind() { return bigBlind; }
        public void setBigBlind(int bigBlind) { this.bigBlind = bigBlind; }

        @Override
        public String toString() {
            return String.format("CreateTableRequest(smallBlind=%d, bigBlind=%d)", smallBlind, bigBlind);
        }
    }
} 