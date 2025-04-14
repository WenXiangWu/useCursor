package com.poker.model;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Hand {
    private final List<Card> holeCards;  // 底牌
    private final List<Card> communityCards;  // 公共牌
    private HandRank handRank;  // 牌型大小
    private List<Card> bestHand;  // 最佳组合

    public Hand() {
        this.holeCards = new ArrayList<>(2);
        this.communityCards = new ArrayList<>(5);
        this.handRank = null;
        this.bestHand = null;
    }

    public void addHoleCard(Card card) {
        if (holeCards.size() < 2) {
            holeCards.add(card);
        } else {
            throw new IllegalStateException("Cannot add more than 2 hole cards");
        }
    }

    public void addCommunityCard(Card card) {
        if (communityCards.size() < 5) {
            communityCards.add(card);
        } else {
            throw new IllegalStateException("Cannot add more than 5 community cards");
        }
    }

    public void clear() {
        holeCards.clear();
        communityCards.clear();
        handRank = null;
        bestHand = null;
    }

    public boolean isComplete() {
        return holeCards.size() == 2 && communityCards.size() == 5;
    }

    public List<Card> getAllCards() {
        List<Card> allCards = new ArrayList<>(holeCards);
        allCards.addAll(communityCards);
        return allCards;
    }

    public void setHandRank(HandRank handRank) {
        this.handRank = handRank;
    }

    public void setBestHand(List<Card> bestHand) {
        this.bestHand = bestHand;
    }

    public HandRank getHandRank() {
        return handRank;
    }

    public List<Card> getBestHand() {
        return bestHand;
    }
} 