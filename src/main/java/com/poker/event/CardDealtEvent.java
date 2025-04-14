package com.poker.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CardDealtEvent extends ApplicationEvent {
    private final String gameId;
    private final String playerId;
    private final String[] cards;
    private final boolean isPrivate;  // 是否为私有牌（底牌）
    private final int cardCount;      // 本次发牌数量
    private final long dealTime;      // 发牌时间戳

    public CardDealtEvent(Object source, String gameId, String playerId, String[] cards, 
                         boolean isPrivate, int cardCount, long dealTime) {
        super(source);
        this.gameId = gameId;
        this.playerId = playerId;
        this.cards = cards;
        this.isPrivate = isPrivate;
        this.cardCount = cardCount;
        this.dealTime = dealTime;
    }
}