package com.poker.model;

import com.poker.event.*;

public interface GameEventListener {
    void onGameStarted(GameStartedEvent event);
    void onGameEnded(GameEndedEvent event);
    void onPlayerJoined(PlayerJoinedEvent event);
    void onPlayerLeft(PlayerLeftEvent event);
    void onRoundStarted(RoundStartedEvent event);
    void onPlayerAction(PlayerActionEvent event);
    void onRoundEnded(RoundEndedEvent event);
    void onCardDealt(CardDealtEvent event);
    void onCommunityCardDealt(CommunityCardDealtEvent event);
    void onPlayerEliminated(PlayerEliminatedEvent event);
    void onGameStateChanged(GameStateChangedEvent event);
    void onBlindsChanged(BlindsChangedEvent event);
    void onDealerChanged(DealerChangedEvent event);
    void onPlayerTimeout(PlayerTimeoutEvent event);
    void onChatMessage(ChatMessageEvent event);
    void onBetPlaced(BetPlacedEvent event);
}