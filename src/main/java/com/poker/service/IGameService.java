package com.poker.service;

import com.poker.model.Game;
import com.poker.model.Player;
import com.poker.model.GameConfig;
import java.util.Collection;

/**
 * 游戏服务接口，定义了游戏相关的所有操作
 */
public interface IGameService {
    /**
     * 获取当前游戏
     */
    Game getCurrentGame();

    /**
     * 创建新游戏
     */
    Game createGame(GameConfig config);

    /**
     * 加入游戏
     */
    void joinGame(String gameId, Player player);

    /**
     * 离开游戏
     */
    void leaveGame(String gameId, Player player);

    /**
     * 处理玩家行动
     */
    void handlePlayerAction(String gameId, Player player, String action, Integer amount);

    /**
     * 获取玩家信息
     */
    Player getPlayer(String playerId);

    /**
     * 创建新玩家
     */
    Player createPlayer(String username, int startingChips);

    /**
     * 开始游戏
     */
    void startGame(String gameId, String playerId);

    /**
     * 更新盲注
     */
    void updateBlinds(String gameId, int smallBlind, int bigBlind);

    /**
     * 处理玩家超时
     */
    void handlePlayerTimeout(String gameId, String playerId);

    /**
     * 处理玩家断开连接
     */
    void handlePlayerDisconnect(String gameId, String playerId);

    /**
     * 处理玩家重新连接
     */
    void handlePlayerReconnect(String gameId, String playerId);

    /**
     * 获取所有游戏
     */
    Collection<Game> getAllGames();
} 