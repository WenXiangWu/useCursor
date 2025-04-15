package com.poker.event;

import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GameEventManager {

    @EventListener
    public void handleGameEvent(GameEvent event) {
        log.info("收到游戏事件: {}", event.getEventType());
        
        GameEvent.GameEventType eventType = event.getEventType();
        switch (eventType) {
            case CREATED:
                handleGameCreated(event);
                break;
            case STARTED:
                handleGameStarted(event);
                break;
            case PLAYER_JOINED:
                handlePlayerJoined(event);
                break;
            case PLAYER_LEFT:
                handlePlayerLeft(event);
                break;
            case PLAYER_ACTION:
                handlePlayerAction(event);
                break;
            case ROUND_ENDED:
                handleRoundEnded(event);
                break;
            case GAME_ENDED:
                handleGameEnded(event);
                break;
            case PLAYER_TIMEOUT:
                handlePlayerTimeout(event);
                break;
            default:
                log.warn("未处理的事件类型: {}", eventType);
        }
    }

    private void handleGameCreated(GameEvent event) {
        log.info("游戏创建: {}", event.getGame().getId());
    }

    private void handleGameStarted(GameEvent event) {
        log.info("游戏开始: {}", event.getGame().getId());
    }

    private void handlePlayerJoined(GameEvent event) {
        log.info("玩家加入: {} -> {}", event.getPlayer().getUsername(), event.getGame().getId());
    }

    private void handlePlayerLeft(GameEvent event) {
        log.info("玩家离开: {} -> {}", event.getPlayer().getUsername(), event.getGame().getId());
    }

    private void handlePlayerAction(GameEvent event) {
        log.info("玩家行动: {} -> {} ({})", 
            event.getPlayer().getUsername(), 
            event.getGame().getId(), 
            event.getAction());
    }

    private void handleRoundEnded(GameEvent event) {
        log.info("回合结束: {}", event.getGame().getId());
    }

    private void handleGameEnded(GameEvent event) {
        log.info("游戏结束: {}", event.getGame().getId());
    }

    private void handlePlayerTimeout(GameEvent event) {
        log.info("玩家超时: {} -> {}", event.getPlayer().getUsername(), event.getGame().getId());
    }
}