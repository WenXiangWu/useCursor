package com.poker.model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 游戏类，管理游戏的状态和逻辑
 */
public class Game {
    private final String id;
    private final GameConfig config;
    private GameStatus status;
    private final List<Player> players;
    private final Map<String, Player> playerMap;
    private final List<Card> deck;
    private final List<Card> communityCards;
    private final List<ChatMessage> chatMessages;
    private final List<GameAction> history;
    private int currentRound;
    private int pot;
    private Player currentPlayer;
    private Player dealer;
    private int smallBlind;
    private int bigBlind;
    private long lastActionTime;
    private final long createdAt;

    public Game(GameConfig config) {
        this.id = UUID.randomUUID().toString();
        this.config = config;
        this.status = GameStatus.WAITING;
        this.players = new CopyOnWriteArrayList<>();
        this.playerMap = new ConcurrentHashMap<>();
        this.deck = new ArrayList<>();
        this.communityCards = new ArrayList<>();
        this.chatMessages = new CopyOnWriteArrayList<>();
        this.history = new CopyOnWriteArrayList<>();
        this.currentRound = 0;
        this.pot = 0;
        this.smallBlind = config.getSmallBlind();
        this.bigBlind = config.getBigBlind();
        this.createdAt = System.currentTimeMillis();
        this.lastActionTime = createdAt;
        initializeDeck();
    }

    private void initializeDeck() {
        deck.clear();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(deck);
    }

    public boolean canJoin(Player player) {
        return status == GameStatus.WAITING 
            && players.size() < config.getMaxPlayers() 
            && !playerMap.containsKey(player.getId());
    }

    public void addPlayer(Player player) {
        if (canJoin(player)) {
            players.add(player);
            playerMap.put(player.getId(), player);
            addHistory("PLAYER_JOINED", player, null);
        }
    }

    public void removePlayer(Player player) {
        if (players.remove(player)) {
            playerMap.remove(player.getId());
            addHistory("PLAYER_LEFT", player, null);
            if (status == GameStatus.PLAYING && players.size() < config.getMinPlayers()) {
                pause();
            }
        }
    }

    public boolean canStart(String playerId) {
        Player player = getPlayer(playerId);
        return status == GameStatus.WAITING 
            && player != null 
            && player.isHost() 
            && players.size() >= config.getMinPlayers();
    }

    public void start() {
        if (status == GameStatus.WAITING) {
            status = GameStatus.PLAYING;
            currentRound = 1;
            dealer = players.get(0);
            dealCards();
            postBlinds();
            currentPlayer = getNextPlayer(dealer);
            addHistory("GAME_STARTED", null, null);
            updateLastActionTime();
        }
    }

    public boolean canPause(String playerId) {
        Player player = getPlayer(playerId);
        return status == GameStatus.PLAYING 
            && player != null 
            && player.isHost();
    }

    public void pause() {
        if (status == GameStatus.PLAYING) {
            status = GameStatus.PAUSED;
            addHistory("GAME_PAUSED", null, null);
            updateLastActionTime();
        }
    }

    public boolean canResume(String playerId) {
        Player player = getPlayer(playerId);
        return status == GameStatus.PAUSED 
            && player != null 
            && player.isHost();
    }

    public void resume() {
        if (status == GameStatus.PAUSED) {
            status = GameStatus.PLAYING;
            addHistory("GAME_RESUMED", null, null);
            updateLastActionTime();
        }
    }

    public void handleFold(Player player) {
        if (isValidAction(player)) {
            player.fold();
            addHistory("FOLD", player, null);
            moveToNextPlayer();
        }
    }

    public void handleCheck(Player player) {
        if (isValidAction(player) && canCheck(player)) {
            addHistory("CHECK", player, null);
            moveToNextPlayer();
        }
    }

    public void handleCall(Player player) {
        if (isValidAction(player)) {
            int callAmount = getCallAmount(player);
            if (player.getChips() >= callAmount) {
                player.bet(callAmount);
                pot += callAmount;
                addHistory("CALL", player, callAmount);
                moveToNextPlayer();
            }
        }
    }

    public void handleRaise(Player player, int amount) {
        if (isValidAction(player) && canRaise(player, amount)) {
            player.bet(amount);
            pot += amount;
            addHistory("RAISE", player, amount);
            moveToNextPlayer();
        }
    }

    public void handleAllIn(Player player) {
        if (isValidAction(player)) {
            int amount = player.getChips();
            player.bet(amount);
            pot += amount;
            addHistory("ALL_IN", player, amount);
            moveToNextPlayer();
        }
    }

    public void handlePlayerTimeout(Player player) {
        if (status == GameStatus.PLAYING && currentPlayer == player) {
            player.fold();
            addHistory("TIMEOUT_FOLD", player, null);
            moveToNextPlayer();
        }
    }

    public void handlePlayerDisconnect(Player player) {
        if (status == GameStatus.PLAYING && currentPlayer == player) {
            player.fold();
            addHistory("DISCONNECT_FOLD", player, null);
            moveToNextPlayer();
        }
    }

    public void handlePlayerReconnect(Player player) {
        addHistory("PLAYER_RECONNECTED", player, null);
    }

    public boolean canUpdateConfig() {
        return status == GameStatus.WAITING;
    }

    public void updateConfig(GameConfig newConfig) {
        if (canUpdateConfig()) {
            // 更新配置
            this.smallBlind = newConfig.getSmallBlind();
            this.bigBlind = newConfig.getBigBlind();
            addHistory("CONFIG_UPDATED", null, newConfig);
        }
    }

    public void addChatMessage(ChatMessage message) {
        chatMessages.add(message);
        if (chatMessages.size() > config.getMaxChatHistory()) {
            chatMessages.remove(0);
        }
    }

    public boolean isExpired(long currentTime) {
        return status == GameStatus.WAITING 
            && currentTime - lastActionTime > config.getGameTimeout() * 1000;
    }

    private void dealCards() {
        for (Player player : players) {
            player.clearCards();
            player.addCard(deck.remove(0));
            player.addCard(deck.remove(0));
        }
    }

    private void postBlinds() {
        int sbIndex = (players.indexOf(dealer) + 1) % players.size();
        int bbIndex = (sbIndex + 1) % players.size();
        
        Player sbPlayer = players.get(sbIndex);
        Player bbPlayer = players.get(bbIndex);
        
        sbPlayer.bet(smallBlind);
        bbPlayer.bet(bigBlind);
        pot = smallBlind + bigBlind;
        
        addHistory("BLINDS_POSTED", null, Map.of(
            "smallBlind", smallBlind,
            "bigBlind", bigBlind
        ));
    }

    private void moveToNextPlayer() {
        currentPlayer = getNextPlayer(currentPlayer);
        if (isRoundComplete()) {
            if (currentRound < 4) {
                dealCommunityCards();
                currentRound++;
            } else {
                endRound();
            }
        }
        updateLastActionTime();
    }

    private Player getNextPlayer(Player current) {
        int currentIndex = players.indexOf(current);
        int nextIndex = (currentIndex + 1) % players.size();
        Player next = players.get(nextIndex);
        
        while (next.hasFolded() || next.isAllIn()) {
            nextIndex = (nextIndex + 1) % players.size();
            next = players.get(nextIndex);
            if (next == current) {
                break;
            }
        }
        
        return next;
    }

    private void dealCommunityCards() {
        int cardsToDeal = currentRound == 1 ? 3 : 1;
        for (int i = 0; i < cardsToDeal; i++) {
            communityCards.add(deck.remove(0));
        }
        addHistory("COMMUNITY_CARDS_DEALT", null, communityCards);
    }

    private void endRound() {
        // TODO: 实现回合结束逻辑，包括比牌和分发奖池
        status = GameStatus.WAITING;
        addHistory("ROUND_ENDED", null, null);
    }

    private boolean isValidAction(Player player) {
        return status == GameStatus.PLAYING 
            && currentPlayer == player 
            && !player.hasFolded() 
            && !player.isAllIn();
    }

    private boolean canCheck(Player player) {
        return pot == 0 || player.getCurrentBet() == getMaxBet();
    }

    private boolean canRaise(Player player, int amount) {
        return amount > getMaxBet() 
            && amount <= player.getChips() 
            && amount >= bigBlind;
    }

    private int getCallAmount(Player player) {
        return getMaxBet() - player.getCurrentBet();
    }

    private int getMaxBet() {
        return players.stream()
            .mapToInt(Player::getCurrentBet)
            .max()
            .orElse(0);
    }

    private boolean isRoundComplete() {
        return players.stream()
            .filter(p -> !p.hasFolded() && !p.isAllIn())
            .allMatch(p -> p.getCurrentBet() == getMaxBet());
    }

    private void updateLastActionTime() {
        this.lastActionTime = System.currentTimeMillis();
    }

    private void addHistory(String type, Player player, Object data) {
        history.add(new GameAction(type, player, data, System.currentTimeMillis()));
    }

    // Getters
    public String getId() { return id; }
    public GameStatus getStatus() { return status; }
    public List<Player> getPlayers() { return players; }
    public Player getPlayer(String playerId) { return playerMap.get(playerId); }
    public int getCurrentRound() { return currentRound; }
    public int getPot() { return pot; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public List<Card> getCommunityCards() { return communityCards; }
    public List<ChatMessage> getChatMessages() { return chatMessages; }
    public List<GameAction> getHistory() { return history; }
    public GameAction getLastAction() { return history.isEmpty() ? null : history.get(history.size() - 1); }
    public long getLastActionTime() { return lastActionTime; }
    public long getCreatedAt() { return createdAt; }
} 