package com.poker.model;

import java.io.Serializable;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 游戏配置类，用于配置游戏参数
 */
@Data
@Component
public class GameConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private int smallBlind;
    private int bigBlind;
    private int minPlayers;
    private int maxPlayers;
    private int maxChatHistory;
    private int gameTimeout;  // 游戏超时时间（秒）

    public GameConfig() {
        // 默认配置
        this.smallBlind = 10;
        this.bigBlind = 20;
        this.minPlayers = 2;
        this.maxPlayers = 9;
        this.maxChatHistory = 100;
        this.gameTimeout = 30;
    }

    public GameConfig(int smallBlind, int bigBlind, int minPlayers, int maxPlayers, 
                     int maxChatHistory, int gameTimeout) {
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.maxChatHistory = maxChatHistory;
        this.gameTimeout = gameTimeout;
    }

    // 构建器模式，用于创建自定义配置
    public static GameConfigBuilder builder() {
        return new GameConfigBuilder();
    }

    public static class GameConfigBuilder {
        private int smallBlind = 10;
        private int bigBlind = 20;
        private int minPlayers = 2;
        private int maxPlayers = 9;
        private int maxChatHistory = 100;
        private int gameTimeout = 30;

        public GameConfigBuilder smallBlind(int smallBlind) {
            this.smallBlind = smallBlind;
            return this;
        }

        public GameConfigBuilder bigBlind(int bigBlind) {
            this.bigBlind = bigBlind;
            return this;
        }

        public GameConfigBuilder minPlayers(int minPlayers) {
            this.minPlayers = minPlayers;
            return this;
        }

        public GameConfigBuilder maxPlayers(int maxPlayers) {
            this.maxPlayers = maxPlayers;
            return this;
        }

        public GameConfigBuilder maxChatHistory(int maxChatHistory) {
            this.maxChatHistory = maxChatHistory;
            return this;
        }

        public GameConfigBuilder gameTimeout(int gameTimeout) {
            this.gameTimeout = gameTimeout;
            return this;
        }

        public GameConfig build() {
            return new GameConfig(smallBlind, bigBlind, minPlayers, maxPlayers, 
                                maxChatHistory, gameTimeout);
        }
    }
}