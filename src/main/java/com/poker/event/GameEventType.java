package com.poker.event;

public enum GameEventType {
    CREATED,            // 游戏创建
    STARTED,            // 游戏开始
    ENDED,              // 游戏结束
    PAUSED,             // 游戏暂停
    RESUMED,            // 游戏恢复
    PLAYER_JOINED,      // 玩家加入
    PLAYER_LEFT,        // 玩家离开
    PLAYER_ACTION,      // 玩家行动
    PLAYER_TIMEOUT,     // 玩家超时
    PLAYER_DISCONNECTED,// 玩家断开连接
    PLAYER_RECONNECTED, // 玩家重新连接
    ROUND_STARTED,      // 回合开始
    ROUND_ENDED,        // 回合结束
    BETTING_STARTED,    // 下注开始
    BETTING_ENDED,      // 下注结束
    CARDS_DEALT,        // 发牌
    SHOWDOWN,           // 摊牌
    CONFIG_UPDATED,     // 配置更新
    ERROR              // 错误
}