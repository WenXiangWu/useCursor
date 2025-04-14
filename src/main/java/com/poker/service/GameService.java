package com.poker.service;

import com.poker.model.Game;
import com.poker.model.GameConfig;
import com.poker.model.Player;

import java.util.List;
import java.util.Map;

/**
 * 游戏服务接口，定义游戏相关的核心业务方法
 */
public interface GameService {
    
    /**
     * 获取可用的游戏列表
     */
    List<Game> getAvailableGames();
    
    /**
     * 创建新游戏
     */
    Game createGame(GameConfig gameConfig);
    
    /**
     * 获取游戏信息
     */
    Game getGame(String gameId);
    
    /**
     * 加入游戏
     */
    Game joinGame(String gameId, Player player);
    
    /**
     * 离开游戏
     */
    boolean leaveGame(String gameId, String playerId);
    
    /**
     * 开始游戏
     */
    boolean startGame(String gameId, String playerId);
    
    /**
     * 暂停游戏
     */
    boolean pauseGame(String gameId, String playerId);
    
    /**
     * 恢复游戏
     */
    boolean resumeGame(String gameId, String playerId);
    
    /**
     * 处理玩家操作
     */
    void handlePlayerAction(String gameId, String playerId, String actionType, Object actionData);
    
    /**
     * 处理聊天消息
     */
    void handleChatMessage(String gameId, String playerId, String content);
    
    /**
     * 获取游戏状态
     */
    Map<String, Object> getGameStatus(String gameId);
    
    /**
     * 处理玩家超时
     */
    void handlePlayerTimeout(String gameId, String playerId);
    
    /**
     * 处理玩家断线
     */
    void handlePlayerDisconnect(String gameId, String playerId);
    
    /**
     * 处理玩家重连
     */
    void handlePlayerReconnect(String gameId, String playerId);
    
    /**
     * 更新游戏配置
     */
    boolean updateGameConfig(String gameId, GameConfig gameConfig);
    
    /**
     * 获取游戏历史记录
     */
    List<Map<String, Object>> getGameHistory(String gameId);
    
    /**
     * 保存游戏状态
     */
    void saveGameState(String gameId);
    
    /**
     * 加载游戏状态
     */
    boolean loadGameState(String gameId);
    
    /**
     * 清理过期游戏
     */
    void cleanupExpiredGames();
} 