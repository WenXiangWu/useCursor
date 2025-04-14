package com.poker.model;

import java.util.*;
import java.util.stream.Collectors;

public class HandEvaluator {
    public static HandRank evaluateHand(Hand hand) {
        List<Card> allCards = hand.getAllCards();
        
        // 检查皇家同花顺
        if (isRoyalFlush(allCards)) {
            hand.setBestHand(getRoyalFlush(allCards));
            return HandRank.ROYAL_FLUSH;
        }
        
        // 检查同花顺
        if (isStraightFlush(allCards)) {
            hand.setBestHand(getStraightFlush(allCards));
            return HandRank.STRAIGHT_FLUSH;
        }
        
        // 检查四条
        if (isFourOfAKind(allCards)) {
            hand.setBestHand(getFourOfAKind(allCards));
            return HandRank.FOUR_OF_A_KIND;
        }
        
        // 检查葫芦
        if (isFullHouse(allCards)) {
            hand.setBestHand(getFullHouse(allCards));
            return HandRank.FULL_HOUSE;
        }
        
        // 检查同花
        if (isFlush(allCards)) {
            hand.setBestHand(getFlush(allCards));
            return HandRank.FLUSH;
        }
        
        // 检查顺子
        if (isStraight(allCards)) {
            hand.setBestHand(getStraight(allCards));
            return HandRank.STRAIGHT;
        }
        
        // 检查三条
        if (isThreeOfAKind(allCards)) {
            hand.setBestHand(getThreeOfAKind(allCards));
            return HandRank.THREE_OF_A_KIND;
        }
        
        // 检查两对
        if (isTwoPair(allCards)) {
            hand.setBestHand(getTwoPair(allCards));
            return HandRank.TWO_PAIR;
        }
        
        // 检查一对
        if (isPair(allCards)) {
            hand.setBestHand(getPair(allCards));
            return HandRank.PAIR;
        }
        
        // 高牌
        hand.setBestHand(getHighCard(allCards));
        return HandRank.HIGH_CARD;
    }

    private static boolean isRoyalFlush(List<Card> cards) {
        return isStraightFlush(cards) && 
               cards.stream().anyMatch(card -> card.getRank() == Card.Rank.ACE) &&
               cards.stream().anyMatch(card -> card.getRank() == Card.Rank.KING);
    }

    private static boolean isStraightFlush(List<Card> cards) {
        return isFlush(cards) && isStraight(cards);
    }

    private static boolean isFourOfAKind(List<Card> cards) {
        return cards.stream()
                   .collect(Collectors.groupingBy(Card::getRank))
                   .values()
                   .stream()
                   .anyMatch(group -> group.size() >= 4);
    }

    private static boolean isFullHouse(List<Card> cards) {
        Map<Card.Rank, Long> rankCounts = cards.stream()
                                             .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));
        return rankCounts.containsValue(3L) && rankCounts.containsValue(2L);
    }

    private static boolean isFlush(List<Card> cards) {
        return cards.stream()
                   .collect(Collectors.groupingBy(Card::getSuit))
                   .values()
                   .stream()
                   .anyMatch(group -> group.size() >= 5);
    }

    private static boolean isStraight(List<Card> cards) {
        List<Integer> values = cards.stream()
                                  .map(card -> card.getRank().getValue())
                                  .distinct()
                                  .sorted()
                                  .collect(Collectors.toList());
        
        // 检查A-5顺子
        if (values.contains(14) && values.contains(2) && values.contains(3) && 
            values.contains(4) && values.contains(5)) {
            return true;
        }
        
        // 检查普通顺子
        for (int i = 0; i <= values.size() - 5; i++) {
            if (values.get(i + 4) - values.get(i) == 4) {
                return true;
            }
        }
        return false;
    }

    private static boolean isThreeOfAKind(List<Card> cards) {
        return cards.stream()
                   .collect(Collectors.groupingBy(Card::getRank))
                   .values()
                   .stream()
                   .anyMatch(group -> group.size() >= 3);
    }

    private static boolean isTwoPair(List<Card> cards) {
        return cards.stream()
                   .collect(Collectors.groupingBy(Card::getRank))
                   .values()
                   .stream()
                   .filter(group -> group.size() >= 2)
                   .count() >= 2;
    }

    private static boolean isPair(List<Card> cards) {
        return cards.stream()
                   .collect(Collectors.groupingBy(Card::getRank))
                   .values()
                   .stream()
                   .anyMatch(group -> group.size() >= 2);
    }

    private static List<Card> getRoyalFlush(List<Card> cards) {
        return getStraightFlush(cards).stream()
                                    .filter(card -> card.getRank().getValue() >= 10)
                                    .collect(Collectors.toList());
    }

    private static List<Card> getStraightFlush(List<Card> cards) {
        return cards.stream()
                   .collect(Collectors.groupingBy(Card::getSuit))
                   .values()
                   .stream()
                   .filter(group -> group.size() >= 5)
                   .findFirst()
                   .map(group -> group.stream()
                                    .sorted(Comparator.comparing(card -> card.getRank().getValue()))
                                    .collect(Collectors.toList()))
                   .orElse(new ArrayList<>());
    }

    private static List<Card> getFourOfAKind(List<Card> cards) {
        return cards.stream()
                   .collect(Collectors.groupingBy(Card::getRank))
                   .values()
                   .stream()
                   .filter(group -> group.size() >= 4)
                   .findFirst()
                   .orElse(new ArrayList<>());
    }

    private static List<Card> getFullHouse(List<Card> cards) {
        List<Card> threeOfAKind = getThreeOfAKind(cards);
        List<Card> pair = cards.stream()
                              .filter(card -> !threeOfAKind.contains(card))
                              .collect(Collectors.groupingBy(Card::getRank))
                              .values()
                              .stream()
                              .filter(group -> group.size() >= 2)
                              .findFirst()
                              .orElse(new ArrayList<>());
        
        List<Card> result = new ArrayList<>(threeOfAKind);
        result.addAll(pair.subList(0, 2));
        return result;
    }

    private static List<Card> getFlush(List<Card> cards) {
        return cards.stream()
                   .collect(Collectors.groupingBy(Card::getSuit))
                   .values()
                   .stream()
                   .filter(group -> group.size() >= 5)
                   .findFirst()
                   .map(group -> group.stream()
                                    .sorted(Comparator.comparing(card -> card.getRank().getValue()))
                                    .limit(5)
                                    .collect(Collectors.toList()))
                   .orElse(new ArrayList<>());
    }

    private static List<Card> getStraight(List<Card> cards) {
        List<Integer> values = cards.stream()
                                  .map(card -> card.getRank().getValue())
                                  .distinct()
                                  .sorted()
                                  .collect(Collectors.toList());
        
        // 检查A-5顺子
        if (values.contains(14) && values.contains(2) && values.contains(3) && 
            values.contains(4) && values.contains(5)) {
            return cards.stream()
                       .filter(card -> card.getRank().getValue() <= 5 || card.getRank().getValue() == 14)
                       .sorted(Comparator.comparing(card -> card.getRank().getValue()))
                       .limit(5)
                       .collect(Collectors.toList());
        }
        
        // 检查普通顺子
        for (int i = 0; i <= values.size() - 5; i++) {
            if (values.get(i + 4) - values.get(i) == 4) {
                final int start = values.get(i);
                return cards.stream()
                           .filter(card -> card.getRank().getValue() >= start && 
                                         card.getRank().getValue() <= start + 4)
                           .sorted(Comparator.comparing(card -> card.getRank().getValue()))
                           .limit(5)
                           .collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    private static List<Card> getThreeOfAKind(List<Card> cards) {
        return cards.stream()
                   .collect(Collectors.groupingBy(Card::getRank))
                   .values()
                   .stream()
                   .filter(group -> group.size() >= 3)
                   .findFirst()
                   .orElse(new ArrayList<>());
    }

    private static List<Card> getTwoPair(List<Card> cards) {
        return cards.stream()
                   .collect(Collectors.groupingBy(Card::getRank))
                   .values()
                   .stream()
                   .filter(group -> group.size() >= 2)
                   .sorted(Comparator.comparing(group -> group.get(0).getRank().getValue()))
                   .limit(2)
                   .flatMap(List::stream)
                   .collect(Collectors.toList());
    }

    private static List<Card> getPair(List<Card> cards) {
        return cards.stream()
                   .collect(Collectors.groupingBy(Card::getRank))
                   .values()
                   .stream()
                   .filter(group -> group.size() >= 2)
                   .findFirst()
                   .orElse(new ArrayList<>());
    }

    private static List<Card> getHighCard(List<Card> cards) {
        return cards.stream()
                   .sorted(Comparator.comparing(card -> card.getRank().getValue()))
                   .limit(5)
                   .collect(Collectors.toList());
    }
} 