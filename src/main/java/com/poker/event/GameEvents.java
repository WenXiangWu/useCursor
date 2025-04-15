package com.poker.event;

import com.poker.model.GameConfig;
import com.poker.model.Player;
import lombok.Getter;
import java.util.List;

@Getter
public class GameEvents {
    // 游戏状态变更事件
    public static class GameStateChanged {
        private String gameId;
        private String state;
        private Object data;

        public GameStateChanged(String gameId, String state, Object data) {
            this.gameId = gameId;
            this.state = state;
            this.data = data;
        }

        public String getGameId() { return gameId; }
        public String getState() { return state; }
        public Object getData() { return data; }
    }

    // 聊天消息事件
    public static class ChatMessageEvent {
        private String gameId;
        private String playerId;
        private String message;
        private boolean isPrivate;

        public ChatMessageEvent(String gameId, String playerId, String message, boolean isPrivate) {
            this.gameId = gameId;
            this.playerId = playerId;
            this.message = message;
            this.isPrivate = isPrivate;
        }

        public String getGameId() { return gameId; }
        public String getPlayerId() { return playerId; }
        public String getMessage() { return message; }
        public boolean isPrivate() { return isPrivate; }
    }

    // 游戏创建事件
    public static class GameCreatedEvent {
        private final String gameId;
        private final GameConfig config;

        public GameCreatedEvent(String gameId, GameConfig config) {
            this.gameId = gameId;
            this.config = config;
        }
    }

    // 游戏开始事件
    public static class GameStartedEvent {
        private final String gameId;
        private final List<Player> players;
        private final long timestamp;

        public GameStartedEvent(String gameId, List<Player> players) {
            this.gameId = gameId;
            this.players = players;
            this.timestamp = System.currentTimeMillis();
        }
    }

    // 游戏结束事件
    public static class GameEndedEvent {
        private final String gameId;
        private final Player winner;
        private final int finalPot;
        private final long timestamp;

        public GameEndedEvent(String gameId, Player winner, int finalPot) {
            this.gameId = gameId;
            this.winner = winner;
            this.finalPot = finalPot;
            this.timestamp = System.currentTimeMillis();
        }
    }

    // 玩家加入事件
    public static class PlayerJoinedEvent {
        private final String gameId;
        private final Player player;
        private final long timestamp;

        public PlayerJoinedEvent(String gameId, Player player) {
            this.gameId = gameId;
            this.player = player;
            this.timestamp = System.currentTimeMillis();
        }
    }

    // 玩家离开事件
    public static class PlayerLeftEvent {
        private final String gameId;
        private final Player player;
        private final long timestamp;

        public PlayerLeftEvent(String gameId, Player player) {
            this.gameId = gameId;
            this.player = player;
            this.timestamp = System.currentTimeMillis();
        }
    }

    // 回合开始事件
    public static class RoundStartedEvent {
        private final String gameId;
        private final int roundNumber;
        private final Player dealer;
        private final long timestamp;

        public RoundStartedEvent(String gameId, int roundNumber, Player dealer) {
            this.gameId = gameId;
            this.roundNumber = roundNumber;
            this.dealer = dealer;
            this.timestamp = System.currentTimeMillis();
        }
    }

    // 玩家动作事件
    public static class PlayerActionEvent {
        private final String gameId;
        private final Player player;
        private final String action;
        private final int amount;
        private final long timestamp;

        public PlayerActionEvent(String gameId, Player player, String action, int amount) {
            this.gameId = gameId;
            this.player = player;
            this.action = action;
            this.amount = amount;
            this.timestamp = System.currentTimeMillis();
        }
    }

    // 玩家超时事件
    public static class PlayerTimeoutEvent {
        private final String gameId;
        private final Player player;
        private final long timestamp;

        public PlayerTimeoutEvent(String gameId, Player player) {
            this.gameId = gameId;
            this.player = player;
            this.timestamp = System.currentTimeMillis();
        }
    }

    // 玩家断开连接事件
    public static class PlayerDisconnectedEvent {
        private final String gameId;
        private final Player player;
        private final long timestamp;

        public PlayerDisconnectedEvent(String gameId, Player player) {
            this.gameId = gameId;
            this.player = player;
            this.timestamp = System.currentTimeMillis();
        }
    }

    // 玩家重新连接事件
    public static class PlayerReconnectedEvent {
        private final String gameId;
        private final Player player;
        private final long timestamp;

        public PlayerReconnectedEvent(String gameId, Player player) {
            this.gameId = gameId;
            this.player = player;
            this.timestamp = System.currentTimeMillis();
        }
    }

    // 游戏配置更新事件
    public static class GameConfigUpdatedEvent {
        private final String gameId;
        private final GameConfig newConfig;
        private final long timestamp;

        public GameConfigUpdatedEvent(String gameId, GameConfig newConfig) {
            this.gameId = gameId;
            this.newConfig = newConfig;
            this.timestamp = System.currentTimeMillis();
        }
    }

    // 游戏状态保存事件
    public static class GameStateSavedEvent {
        private final String gameId;
        private final String state;
        private final long timestamp;

        public GameStateSavedEvent(String gameId, String state) {
            this.gameId = gameId;
            this.state = state;
            this.timestamp = System.currentTimeMillis();
        }
    }
} 