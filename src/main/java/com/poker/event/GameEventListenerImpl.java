package com.poker.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GameEventListenerImpl {
    private static final Logger logger = LoggerFactory.getLogger(GameEventListenerImpl.class);

    @EventListener
    public void handleCardsDealtEvent(CardsDealtEvent event) {
        logger.info("发牌 - 游戏ID: {}, 玩家: {}", 
            event.getGame().getId(), 
            event.getPlayer().getUsername());
    }

    @EventListener
    public void handlePlayerActionEvent(PlayerActionEvent event) {
        logger.info("玩家行动 - 游戏ID: {}, 玩家: {}, 行动: {}, 金额: {}", 
            event.getGame().getId(), 
            event.getPlayer().getUsername(), 
            event.getAction(),
            event.getAmount());
    }

    @EventListener
    public void handlePlayerFoldedEvent(PlayerFoldedEvent event) {
        logger.info("玩家弃牌 - 游戏ID: {}, 玩家: {}", 
            event.getGameId(), 
            event.getPlayer().getUsername());
    }

    @EventListener
    public void handleRoundEndedEvent(RoundEndedEvent event) {
        logger.info("一轮结束 - 游戏ID: {}, 轮数: {}, 获胜者ID: {}, 奖池: {}", 
            event.getGameId(),
            event.getRoundNumber(),
            event.getWinnerId(),
            event.getPotAmount());
    }
} 