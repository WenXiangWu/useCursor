package com.poker.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GameEventListenerImpl implements GameEventListener {
    private static final Logger logger = LoggerFactory.getLogger(GameEventListenerImpl.class);

    @Override
    public void onHandStarted(HandStartedEvent event) {
        logger.info("新一轮开始 - 游戏ID: {}, 轮次: {}", event.getGameId(), event.getHandNumber());
    }

    @Override
    public void onCardsDealt(CardsDealtEvent event) {
        logger.info("发牌 - 游戏ID: {}, 玩家ID: {}, 牌数: {}", 
            event.getGameId(), event.getPlayer().getId(), event.getCards().size());
    }

    @Override
    public void onCommunityCardsDealt(CommunityCardsDealtEvent event) {
        logger.info("发公共牌 - 游戏ID: {}, 牌数: {}", 
            event.getGameId(), event.getCards().size());
    }

    @Override
    public void onPlayerAction(PlayerActionEvent event) {
        logger.info("玩家行动 - 游戏ID: {}, 玩家ID: {}, 行动: {}", 
            event.getGameId(), event.getPlayer().getId(), event.getAction());
    }

    @Override
    public void onPlayerFolded(PlayerFoldedEvent event) {
        logger.info("玩家弃牌 - 游戏ID: {}, 玩家ID: {}", 
            event.getGameId(), event.getPlayer().getId());
    }

    @Override
    public void onGameStateChanged(GameStateChangedEvent event) {
        logger.info("游戏状态改变 - 游戏ID: {}, 新状态: {}", 
            event.getGameId(), event.getNewState());
    }

    @Override
    public void onRoundEnded(RoundEndedEvent event) {
        logger.info("一轮结束 - 游戏ID: {}, 获胜者ID: {}, 奖池金额: {}", 
            event.getGameId(), event.getWinner().getId(), event.getPotAmount());
    }
} 