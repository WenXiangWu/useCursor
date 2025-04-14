package com.poker.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
@Setter
public class GameStats {
    private int totalHandsPlayed;
    private int totalRoundsPlayed;
    private int totalPlayers;
    private int totalSpectators;
    private int totalChatMessages;
    private int totalPrivateMessages;
    private int totalTableChats;
    private int totalPlayerNotes;
    private int totalPlayerStats;
    private int totalPlayerHistory;
    private int totalPlayerAchievements;
    private int totalPlayerBadges;
    private int totalPlayerAvatars;
    private int totalPlayerCustomizations;
    private int totalPlayerSounds;
    private int totalPlayerAnimations;
    private int totalPlayerThemes;
    private int totalPlayerLanguages;
    private int totalPlayerTimeZones;
    private int totalPlayerCurrencies;
    private int totalPlayerBets;
    private int totalPlayerFolds;
    private int totalPlayerChecks;
    private int totalPlayerCalls;
    private int totalPlayerRaises;
    private int totalPlayerAllIns;
    private int totalPlayerSitOuts;
    private int totalPlayerLeaves;
    private int totalPlayerRejoins;
    private int totalPlayerTransfers;
    private int totalPlayerTips;
    private Map<String, Integer> playerStats;
    private Map<String, Integer> spectatorStats;
    private Map<String, Integer> chatStats;
    private Map<String, Integer> privateMessageStats;
    private Map<String, Integer> tableChatStats;
    private Map<String, Integer> playerNoteStats;
    private Map<String, Integer> playerStatStats;
    private Map<String, Integer> playerHistoryStats;
    private Map<String, Integer> playerAchievementStats;
    private Map<String, Integer> playerBadgeStats;
    private Map<String, Integer> playerAvatarStats;
    private Map<String, Integer> playerCustomizationStats;
    private Map<String, Integer> playerSoundStats;
    private Map<String, Integer> playerAnimationStats;
    private Map<String, Integer> playerThemeStats;
    private Map<String, Integer> playerLanguageStats;
    private Map<String, Integer> playerTimeZoneStats;
    private Map<String, Integer> playerCurrencyStats;
    private Map<String, Integer> playerBetStats;
    private Map<String, Integer> playerFoldStats;
    private Map<String, Integer> playerCheckStats;
    private Map<String, Integer> playerCallStats;
    private Map<String, Integer> playerRaiseStats;
    private Map<String, Integer> playerAllInStats;
    private Map<String, Integer> playerSitOutStats;
    private Map<String, Integer> playerLeaveStats;
    private Map<String, Integer> playerRejoinStats;
    private Map<String, Integer> playerTransferStats;
    private Map<String, Integer> playerTipStats;

    public GameStats() {
        playerStats = new HashMap<>();
        spectatorStats = new HashMap<>();
        chatStats = new HashMap<>();
        privateMessageStats = new HashMap<>();
        tableChatStats = new HashMap<>();
        playerNoteStats = new HashMap<>();
        playerStatStats = new HashMap<>();
        playerHistoryStats = new HashMap<>();
        playerAchievementStats = new HashMap<>();
        playerBadgeStats = new HashMap<>();
        playerAvatarStats = new HashMap<>();
        playerCustomizationStats = new HashMap<>();
        playerSoundStats = new HashMap<>();
        playerAnimationStats = new HashMap<>();
        playerThemeStats = new HashMap<>();
        playerLanguageStats = new HashMap<>();
        playerTimeZoneStats = new HashMap<>();
        playerCurrencyStats = new HashMap<>();
        playerBetStats = new HashMap<>();
        playerFoldStats = new HashMap<>();
        playerCheckStats = new HashMap<>();
        playerCallStats = new HashMap<>();
        playerRaiseStats = new HashMap<>();
        playerAllInStats = new HashMap<>();
        playerSitOutStats = new HashMap<>();
        playerLeaveStats = new HashMap<>();
        playerRejoinStats = new HashMap<>();
        playerTransferStats = new HashMap<>();
        playerTipStats = new HashMap<>();
    }

    public void incrementTotalHandsPlayed() {
        totalHandsPlayed++;
    }

    public void incrementTotalRoundsPlayed() {
        totalRoundsPlayed++;
    }

    public void incrementTotalPlayers() {
        totalPlayers++;
    }

    public void incrementTotalSpectators() {
        totalSpectators++;
    }

    public void incrementTotalChatMessages() {
        totalChatMessages++;
    }

    public void incrementTotalPrivateMessages() {
        totalPrivateMessages++;
    }

    public void incrementTotalTableChats() {
        totalTableChats++;
    }

    public void incrementTotalPlayerNotes() {
        totalPlayerNotes++;
    }

    public void incrementTotalPlayerStats() {
        totalPlayerStats++;
    }

    public void incrementTotalPlayerHistory() {
        totalPlayerHistory++;
    }

    public void incrementTotalPlayerAchievements() {
        totalPlayerAchievements++;
    }

    public void incrementTotalPlayerBadges() {
        totalPlayerBadges++;
    }

    public void incrementTotalPlayerAvatars() {
        totalPlayerAvatars++;
    }

    public void incrementTotalPlayerCustomizations() {
        totalPlayerCustomizations++;
    }

    public void incrementTotalPlayerSounds() {
        totalPlayerSounds++;
    }

    public void incrementTotalPlayerAnimations() {
        totalPlayerAnimations++;
    }

    public void incrementTotalPlayerThemes() {
        totalPlayerThemes++;
    }

    public void incrementTotalPlayerLanguages() {
        totalPlayerLanguages++;
    }

    public void incrementTotalPlayerTimeZones() {
        totalPlayerTimeZones++;
    }

    public void incrementTotalPlayerCurrencies() {
        totalPlayerCurrencies++;
    }

    public void incrementTotalPlayerBets() {
        totalPlayerBets++;
    }

    public void incrementTotalPlayerFolds() {
        totalPlayerFolds++;
    }

    public void incrementTotalPlayerChecks() {
        totalPlayerChecks++;
    }

    public void incrementTotalPlayerCalls() {
        totalPlayerCalls++;
    }

    public void incrementTotalPlayerRaises() {
        totalPlayerRaises++;
    }

    public void incrementTotalPlayerAllIns() {
        totalPlayerAllIns++;
    }

    public void incrementTotalPlayerSitOuts() {
        totalPlayerSitOuts++;
    }

    public void incrementTotalPlayerLeaves() {
        totalPlayerLeaves++;
    }

    public void incrementTotalPlayerRejoins() {
        totalPlayerRejoins++;
    }

    public void incrementTotalPlayerTransfers() {
        totalPlayerTransfers++;
    }

    public void incrementTotalPlayerTips() {
        totalPlayerTips++;
    }

    public void incrementPlayerStats(String playerId) {
        playerStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementSpectatorStats(String spectatorId) {
        spectatorStats.merge(spectatorId, 1, Integer::sum);
    }

    public void incrementChatStats(String playerId) {
        chatStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPrivateMessageStats(String playerId) {
        privateMessageStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementTableChatStats(String playerId) {
        tableChatStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerNoteStats(String playerId) {
        playerNoteStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerStatStats(String playerId) {
        playerStatStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerHistoryStats(String playerId) {
        playerHistoryStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerAchievementStats(String playerId) {
        playerAchievementStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerBadgeStats(String playerId) {
        playerBadgeStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerAvatarStats(String playerId) {
        playerAvatarStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerCustomizationStats(String playerId) {
        playerCustomizationStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerSoundStats(String playerId) {
        playerSoundStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerAnimationStats(String playerId) {
        playerAnimationStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerThemeStats(String playerId) {
        playerThemeStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerLanguageStats(String playerId) {
        playerLanguageStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerTimeZoneStats(String playerId) {
        playerTimeZoneStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerCurrencyStats(String playerId) {
        playerCurrencyStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerBetStats(String playerId) {
        playerBetStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerFoldStats(String playerId) {
        playerFoldStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerCheckStats(String playerId) {
        playerCheckStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerCallStats(String playerId) {
        playerCallStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerRaiseStats(String playerId) {
        playerRaiseStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerAllInStats(String playerId) {
        playerAllInStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerSitOutStats(String playerId) {
        playerSitOutStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerLeaveStats(String playerId) {
        playerLeaveStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerRejoinStats(String playerId) {
        playerRejoinStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerTransferStats(String playerId) {
        playerTransferStats.merge(playerId, 1, Integer::sum);
    }

    public void incrementPlayerTipStats(String playerId) {
        playerTipStats.merge(playerId, 1, Integer::sum);
    }
}