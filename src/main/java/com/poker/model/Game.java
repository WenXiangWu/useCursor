package com.poker.model;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Getter;
import lombok.Setter;

/**
 * 游戏类，管理游戏的状态和逻辑
 */
@Getter
@Setter
public class Game implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private GameConfig config;
    private String gameState; // WAITING, PLAYING, FINISHED
    private List<Player> players;
    private Map<String, Player> playerMap;
    private List<Card> deck;
    private List<Card> communityCards;
    private Map<String, List<Card>> playerHands;
    private List<ChatMessage> chatMessages;
    private List<GameAction> history;
    private int currentRound;
    private int pot;
    private String currentPlayer;
    private Player dealer;
    private int smallBlind;
    private int bigBlind;
    private long lastActionTime;
    private long createdAt;
    private int currentBet;
    private int dealerPosition;
    private GameState state;
    private Map<String, Integer> sidePots;
    private List<GameAction> actionHistory;
    private Map<String, Integer> playerBets;
    private List<String> activePlayers;
    private List<String> foldedPlayers;
    private List<ChatMessage> chatHistory;
    private Date startTime;

    public enum GameState {
        WAITING_FOR_PLAYERS,
        STARTING,
        PREFLOP,
        FLOP,
        TURN,
        RIVER,
        SHOWDOWN,
        FINISHED
    }

    public Game(GameConfig config) {
        this.id = UUID.randomUUID().toString();
        this.config = config;
        this.gameState = "WAITING";
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
        this.currentBet = 0;
        this.dealerPosition = 0;
        this.state = GameState.WAITING_FOR_PLAYERS;
        this.sidePots = new HashMap<>();
        this.actionHistory = new ArrayList<>();
        this.playerBets = new HashMap<>();
        this.activePlayers = new ArrayList<>();
        this.foldedPlayers = new ArrayList<>();
        this.chatHistory = new ArrayList<>();
        this.startTime = new Date();
        initializeDeck();
    }

    private void initializeDeck() {
        deck.clear();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(deck);
    }

    public boolean canJoin(Player player) {
        return players.size() < config.getMaxPlayers() && 
               !players.stream().anyMatch(p -> p.getId().equals(player.getId()));
    }

    public void addPlayer(Player player) {
        if (canJoin(player)) {
            players.add(player);
            playerMap.put(player.getId(), player);
            playerBets.put(player.getId(), 0);
            activePlayers.add(player.getId());
            addHistory("PLAYER_JOINED", player, null);
        }
    }

    public void removePlayer(Player player) {
        if (players.remove(player)) {
            playerMap.remove(player.getId());
            playerBets.remove(player.getId());
            foldedPlayers.add(player.getId());
            activePlayers.remove(player.getId());
            addHistory("PLAYER_LEFT", player, null);
            if (gameState.equals("PLAYING") && players.size() < config.getMinPlayers()) {
                pause();
            }
        }
    }

    public boolean canStart(String playerId) {
        Player player = getPlayer(playerId);
        return players.size() >= config.getMinPlayers() && 
               players.get(0).getId().equals(playerId) && 
               "WAITING".equals(gameState);
    }

    public void start() {
        if (players.size() >= config.getMinPlayers()) {
            gameState = "PLAYING";
            currentRound = 1;
            dealer = players.get(0);
            dealCards();
            postBlinds();
            currentPlayer = players.get(0).getId();
            addHistory("GAME_STARTED", null, null);
            updateLastActionTime();
        }
    }

    public boolean canPause(String playerId) {
        Player player = getPlayer(playerId);
        return gameState.equals("PLAYING") 
            && player != null 
            && player.isHost();
    }

    public void pause() {
        if (gameState.equals("PLAYING")) {
            gameState = "PAUSED";
            addHistory("GAME_PAUSED", null, null);
            updateLastActionTime();
        }
    }

    public boolean canResume(String playerId) {
        Player player = getPlayer(playerId);
        return gameState.equals("PAUSED") 
            && player != null 
            && player.isHost();
    }

    public void resume() {
        if (gameState.equals("PAUSED")) {
            gameState = "PLAYING";
            addHistory("GAME_RESUMED", null, null);
            updateLastActionTime();
        }
    }

    public void handleFold(Player player) {
        if (isValidAction(player)) {
            player.fold();
            foldedPlayers.add(player.getId());
            activePlayers.remove(player.getId());
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
                playerBets.put(player.getId(), currentBet);
                addHistory("CALL", player, callAmount);
                moveToNextPlayer();
            }
        }
    }

    public void handleRaise(Player player, int amount) {
        if (isValidAction(player) && canRaise(player, amount)) {
            player.bet(amount);
            pot += amount;
            playerBets.put(player.getId(), amount);
            currentBet = amount;
            addHistory("RAISE", player, amount);
            moveToNextPlayer();
        }
    }

    public void handleAllIn(Player player) {
        if (isValidAction(player)) {
            int amount = player.getChips();
            player.bet(amount);
            pot += amount;
            playerBets.put(player.getId(), amount);
            currentBet = amount;
            addHistory("ALL_IN", player, amount);
            moveToNextPlayer();
        }
    }

    public void handlePlayerTimeout(Player player) {
        if (gameState.equals("PLAYING") && currentPlayer.equals(player.getId())) {
            player.fold();
            foldedPlayers.add(player.getId());
            activePlayers.remove(player.getId());
            addHistory("TIMEOUT_FOLD", player, null);
            moveToNextPlayer();
        }
    }

    public void handlePlayerDisconnect(Player player) {
        if (gameState.equals("PLAYING") && currentPlayer.equals(player.getId())) {
            player.fold();
            foldedPlayers.add(player.getId());
            activePlayers.remove(player.getId());
            addHistory("DISCONNECT_FOLD", player, null);
            moveToNextPlayer();
        }
    }

    public void handlePlayerReconnect(Player player) {
        addHistory("PLAYER_RECONNECTED", player, null);
    }

    public boolean canUpdateConfig() {
        return gameState.equals("WAITING");
    }

    public void updateConfig(GameConfig newConfig) {
        if (canUpdateConfig()) {
            this.config = newConfig;
            this.smallBlind = newConfig.getSmallBlind();
            this.bigBlind = newConfig.getBigBlind();
            addHistory("CONFIG_UPDATED", null, newConfig);
        }
    }

    public void addChatMessage(ChatMessage message) {
        chatMessages.add(message);
        chatHistory.add(message);
        if (chatMessages.size() > config.getMaxChatHistory()) {
            chatMessages.remove(0);
        }
        if (chatHistory.size() > 100) { // 限制聊天历史记录大小
            chatHistory.remove(0);
        }
    }

    public boolean isExpired(long currentTime) {
        return gameState.equals("WAITING") 
            && currentTime - lastActionTime > config.getGameTimeout() * 1000;
    }

    private void dealCards() {
        for (Player player : players) {
            if (player.isActive()) {
                player.clearHoleCards();
                Card card1 = deck.remove(0);
                Card card2 = deck.remove(0);
                player.setHoleCards(new Card[]{card1, card2});
            }
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
        
        addHistory("BLINDS_POSTED", null, new HashMap<String, String>() {{
            put("smallBlind", String.valueOf(smallBlind));
            put("bigBlind", String.valueOf(bigBlind));
        }});
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

    private String getNextPlayer(String currentPlayerId) {
        int currentIndex = activePlayers.indexOf(currentPlayerId);
        if (currentIndex == -1 || activePlayers.isEmpty()) {
            return null;
        }
        return activePlayers.get((currentIndex + 1) % activePlayers.size());
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
        gameState = "WAITING";
        addHistory("ROUND_ENDED", null, null);
    }

    private boolean isValidAction(Player player) {
        return gameState.equals("PLAYING") 
            && currentPlayer.equals(player.getId()) 
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

    private void addHistory(String action, Player player, Object data) {
        GameAction gameAction = new GameAction(action, player, data);
        gameAction.setTimestamp(System.currentTimeMillis());
        history.add(gameAction);
    }

    // Getters
    public String getId() { return id; }
    public String getGameState() { return gameState; }
    public List<Player> getPlayers() { return players; }
    public Player getPlayer(String playerId) { return playerMap.get(playerId); }
    public int getCurrentRound() { return currentRound; }
    public int getPot() { return pot; }
    public String getCurrentPlayer() { return currentPlayer; }
    public List<Card> getCommunityCards() { return communityCards; }
    public List<ChatMessage> getChatMessages() { return chatMessages; }
    public List<GameAction> getHistory() { return history; }
    public GameAction getLastAction() { return history.isEmpty() ? null : history.get(history.size() - 1); }
    public long getLastActionTime() { return lastActionTime; }
    public long getCreatedAt() { return createdAt; }
    public int getCurrentBet() { return currentBet; }
    public List<String> getActivePlayers() { return activePlayers; }
    public List<String> getFoldedPlayers() { return foldedPlayers; }
    public List<ChatMessage> getChatHistory() { return chatHistory; }
    public Date getStartTime() { return startTime; }
} 