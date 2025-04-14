package com.poker.event;

public interface GameEventListener {
    void onHandStarted(HandStartedEvent event);
    void onCardsDealt(CardsDealtEvent event);
    void onCommunityCardsDealt(CommunityCardsDealtEvent event);
    void onPlayerAction(PlayerActionEvent event);
    void onPlayerFolded(PlayerFoldedEvent event);
    void onGameStateChanged(GameStateChangedEvent event);
    void onRoundEnded(RoundEndedEvent event);
}