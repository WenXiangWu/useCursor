package com.poker.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 游戏配置类，用于配置游戏参数
 */
@Component
@ConfigurationProperties(prefix = "poker.game")
@Getter
@Setter
public class GameConfig {
    private int minPlayers = 2;
    private int maxPlayers = 9;
    private int startingChips = 1000;
    private int smallBlind = 5;
    private int bigBlind = 10;
    private int initialDealerChips = 100;
    private int actionTimeoutSeconds = 30;
    private int maxTimeouts = 3;
    private boolean allowSpectators = true;
    private int maxSpectators = 10;
    private boolean autoMuck = true;
    private boolean showMuckedCards = false;
    private boolean allowChat = true;
    private int maxChatLength = 200;
    private boolean allowEmotes = true;
    private boolean allowPrivateMessages = true;
    private int maxPrivateMessageLength = 100;
    private boolean allowTableChat = true;
    private int maxTableChatLength = 150;
    private boolean allowPlayerNotes = true;
    private int maxPlayerNoteLength = 500;
    private boolean allowPlayerStats = true;
    private boolean showPlayerStats = true;
    private boolean allowPlayerHistory = true;
    private int maxPlayerHistory = 100;
    private boolean allowPlayerAchievements = true;
    private boolean showPlayerAchievements = true;
    private boolean allowPlayerBadges = true;
    private boolean showPlayerBadges = true;
    private boolean allowPlayerAvatar = true;
    private boolean allowPlayerCustomization = true;
    private boolean allowPlayerSound = true;
    private boolean allowPlayerAnimation = true;
    private boolean allowPlayerTheme = true;
    private boolean allowPlayerLanguage = true;
    private boolean allowPlayerTimeZone = true;
    private boolean allowPlayerCurrency = true;
    private boolean allowPlayerBetting = true;
    private boolean allowPlayerFolding = true;
    private boolean allowPlayerChecking = true;
    private boolean allowPlayerCalling = true;
    private boolean allowPlayerRaising = true;
    private boolean allowPlayerAllIn = true;
    private boolean allowPlayerSittingOut = true;
    private boolean allowPlayerLeaving = true;
    private boolean allowPlayerRejoining = true;
    private boolean allowPlayerTransferring = true;
    private boolean allowPlayerTipping = true;
    private boolean allowPlayerChatting = true;
    private boolean allowPlayerEmoting = true;
    private boolean allowPlayerPrivateMessaging = true;
    private boolean allowPlayerTableChatting = true;
    private boolean allowPlayerNoting = true;
    private boolean allowPlayerStatting = true;
    private boolean allowPlayerHistorizing = true;
    private boolean allowPlayerAchieving = true;
    private boolean allowPlayerBadging = true;
    private boolean allowPlayerAvataring = true;
    private boolean allowPlayerCustomizing = true;
    private boolean allowPlayerSounding = true;
    private boolean allowPlayerAnimating = true;
    private boolean allowPlayerTheming = true;
    private boolean allowPlayerLanguaging = true;
    private boolean allowPlayerTimeZoning = true;
    private boolean allowPlayerCurrencying = true;
}