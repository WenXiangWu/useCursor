package com.poker.event;

import com.poker.model.Game;
import com.poker.model.Player;
import com.poker.model.Card;
import lombok.Getter;

@Getter
public class CardsDealtEvent extends GameEvent {
    private final String gameId;
    private final Player player;
    private final Card[] cards;

    public CardsDealtEvent(Object source, Game game, Player player, Card[] cards) {
        super(source, game, player, GameEventType.CARDS_DEALT, null);
        this.gameId = game.getId();
        this.player = player;
        this.cards = cards.clone();
    }

    @Override
    public Game getGame() {
        return super.getGame();
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    public Card[] getCards() {
        return cards.clone();
    }
}