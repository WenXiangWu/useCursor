package com.poker.model;

/**
 * 游戏状态枚举
 */
public enum GameStatus {
    WAITING,    // 等待玩家加入
    PLAYING,    // 游戏进行中
    PAUSED,     // 游戏暂停
    FINISHED    // 游戏结束
} 