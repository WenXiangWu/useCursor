package com.poker.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 扑克牌手牌评估器
 */
public class HandEvaluator {
    public enum HandRank {
        HIGH_CARD(1),
        PAIR(2),
        TWO_PAIR(3),
        THREE_OF_A_KIND(4),
        STRAIGHT(5),
        FLUSH(6),
        FULL_HOUSE(7),
        FOUR_OF_A_KIND(8),
        STRAIGHT_FLUSH(9),
        ROYAL_FLUSH(10);

        private final int value;

        HandRank(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static class HandResult implements Comparable<HandResult> {
        private final HandRank rank;
        private final List<Card> bestHand;
        private final List<Card.Rank> kickers;
        private final int score;

        public HandResult(HandRank rank, List<Card> bestHand, List<Card.Rank> kickers, int score) {
            this.rank = rank;
            this.bestHand = bestHand;
            this.kickers = kickers;
            this.score = score;
        }

        public HandRank getRank() { return rank; }
        public List<Card> getBestHand() { return bestHand; }
        public List<Card.Rank> getKickers() { return kickers; }
        public int getScore() { return score; }

        @Override
        public int compareTo(HandResult other) {
            int rankComparison = Integer.compare(this.rank.getValue(), other.rank.getValue());
            if (rankComparison != 0) {
                return rankComparison;
            }

            // 比较踢脚牌
            for (int i = 0; i < Math.min(kickers.size(), other.kickers.size()); i++) {
                int kickerComparison = Integer.compare(
                    kickers.get(i).getValue(),
                    other.kickers.get(i).getValue()
                );
                if (kickerComparison != 0) {
                    return kickerComparison;
                }
            }

            return Integer.compare(this.score, other.score);
        }
    }

    public HandResult evaluate(List<Card> holeCards, List<Card> communityCards) {
        List<Card> allCards = new ArrayList<>(holeCards);
        allCards.addAll(communityCards);

        // 检查同花顺和皇家同花顺
        HandResult straightFlush = checkStraightFlush(allCards);
        if (straightFlush != null) {
            return straightFlush;
        }

        // 检查四条
        HandResult fourOfAKind = checkFourOfAKind(allCards);
        if (fourOfAKind != null) {
            return fourOfAKind;
        }

        // 检查葫芦
        HandResult fullHouse = checkFullHouse(allCards);
        if (fullHouse != null) {
            return fullHouse;
        }

        // 检查同花
        HandResult flush = checkFlush(allCards);
        if (flush != null) {
            return flush;
        }

        // 检查顺子
        HandResult straight = checkStraight(allCards);
        if (straight != null) {
            return straight;
        }

        // 检查三条
        HandResult threeOfAKind = checkThreeOfAKind(allCards);
        if (threeOfAKind != null) {
            return threeOfAKind;
        }

        // 检查两对
        HandResult twoPair = checkTwoPair(allCards);
        if (twoPair != null) {
            return twoPair;
        }

        // 检查对子
        HandResult pair = checkPair(allCards);
        if (pair != null) {
            return pair;
        }

        // 高牌
        return checkHighCard(allCards);
    }

    private HandResult checkStraightFlush(List<Card> cards) {
        // 按花色分组
        Map<Card.Suit, List<Card>> suitGroups = cards.stream()
            .collect(Collectors.groupingBy(Card::getSuit));

        for (List<Card> sameSuitCards : suitGroups.values()) {
            if (sameSuitCards.size() >= 5) {
                HandResult straight = checkStraight(sameSuitCards);
                if (straight != null) {
                    if (straight.getBestHand().get(0).getRank() == Card.Rank.ACE) {
                        return new HandResult(HandRank.ROYAL_FLUSH, straight.getBestHand(), straight.getKickers(), 0);
                    }
                    return new HandResult(HandRank.STRAIGHT_FLUSH, straight.getBestHand(), straight.getKickers(), 0);
                }
            }
        }
        return null;
    }

    private HandResult checkFourOfAKind(List<Card> cards) {
        Map<Card.Rank, List<Card>> rankGroups = cards.stream()
            .collect(Collectors.groupingBy(Card::getRank));

        for (Map.Entry<Card.Rank, List<Card>> entry : rankGroups.entrySet()) {
            if (entry.getValue().size() == 4) {
                List<Card> bestHand = new ArrayList<>(entry.getValue());
                List<Card.Rank> kickers = cards.stream()
                    .filter(c -> c.getRank() != entry.getKey())
                    .map(Card::getRank)
                    .sorted((r1, r2) -> Integer.compare(r2.getValue(), r1.getValue()))
                    .limit(1)
                    .collect(Collectors.toList());
                return new HandResult(HandRank.FOUR_OF_A_KIND, bestHand, kickers, 0);
            }
        }
        return null;
    }

    private HandResult checkFullHouse(List<Card> cards) {
        Map<Card.Rank, List<Card>> rankGroups = cards.stream()
            .collect(Collectors.groupingBy(Card::getRank));

        Card.Rank threeOfAKindRank = null;
        Card.Rank pairRank = null;

        // 找最大的三条
        for (Map.Entry<Card.Rank, List<Card>> entry : rankGroups.entrySet()) {
            if (entry.getValue().size() >= 3) {
                if (threeOfAKindRank == null || 
                    entry.getKey().getValue() > threeOfAKindRank.getValue()) {
                    threeOfAKindRank = entry.getKey();
                }
            }
        }

        // 找最大的对子
        if (threeOfAKindRank != null) {
            for (Map.Entry<Card.Rank, List<Card>> entry : rankGroups.entrySet()) {
                if (entry.getKey() != threeOfAKindRank && entry.getValue().size() >= 2) {
                    if (pairRank == null || 
                        entry.getKey().getValue() > pairRank.getValue()) {
                        pairRank = entry.getKey();
                    }
                }
            }
        }

        if (threeOfAKindRank != null && pairRank != null) {
            List<Card> bestHand = new ArrayList<>();
            bestHand.addAll(rankGroups.get(threeOfAKindRank).subList(0, 3));
            bestHand.addAll(rankGroups.get(pairRank).subList(0, 2));
            return new HandResult(HandRank.FULL_HOUSE, bestHand, Arrays.asList(threeOfAKindRank, pairRank), 0);
        }
        return null;
    }

    private HandResult checkFlush(List<Card> cards) {
        Map<Card.Suit, List<Card>> suitGroups = cards.stream()
            .collect(Collectors.groupingBy(Card::getSuit));

        for (List<Card> sameSuitCards : suitGroups.values()) {
            if (sameSuitCards.size() >= 5) {
                List<Card> bestFlush = sameSuitCards.stream()
                    .sorted((c1, c2) -> Integer.compare(c2.getRank().getValue(), c1.getRank().getValue()))
                    .limit(5)
                    .collect(Collectors.toList());
                List<Card.Rank> kickers = bestFlush.stream()
                    .map(Card::getRank)
                    .collect(Collectors.toList());
                return new HandResult(HandRank.FLUSH, bestFlush, kickers, 0);
            }
        }
        return null;
    }

    private HandResult checkStraight(List<Card> cards) {
        List<Card> distinctRankCards = cards.stream()
            .sorted((c1, c2) -> Integer.compare(c2.getRank().getValue(), c1.getRank().getValue()))
            .distinct()
            .collect(Collectors.toList());

        // 特殊处理A2345顺子
        boolean hasAce = distinctRankCards.stream()
            .anyMatch(c -> c.getRank() == Card.Rank.ACE);
        if (hasAce) {
            List<Card> aceToFive = distinctRankCards.stream()
                .filter(c -> c.getRank() == Card.Rank.ACE || 
                           c.getRank().getValue() <= 5)
                .collect(Collectors.toList());
            if (aceToFive.size() >= 5) {
                List<Card.Rank> kickers = aceToFive.stream()
                    .map(Card::getRank)
                    .collect(Collectors.toList());
                return new HandResult(HandRank.STRAIGHT, aceToFive.subList(0, 5), kickers, 0);
            }
        }

        // 检查普通顺子
        for (int i = 0; i <= distinctRankCards.size() - 5; i++) {
            boolean isStraight = true;
            for (int j = 0; j < 4; j++) {
                if (distinctRankCards.get(i + j).getRank().getValue() - 
                    distinctRankCards.get(i + j + 1).getRank().getValue() != 1) {
                    isStraight = false;
                    break;
                }
            }
            if (isStraight) {
                List<Card> straightCards = distinctRankCards.subList(i, i + 5);
                List<Card.Rank> kickers = straightCards.stream()
                    .map(Card::getRank)
                    .collect(Collectors.toList());
                return new HandResult(HandRank.STRAIGHT, straightCards, kickers, 0);
            }
        }
        return null;
    }

    private HandResult checkThreeOfAKind(List<Card> cards) {
        Map<Card.Rank, List<Card>> rankGroups = cards.stream()
            .collect(Collectors.groupingBy(Card::getRank));

        for (Map.Entry<Card.Rank, List<Card>> entry : rankGroups.entrySet()) {
            if (entry.getValue().size() == 3) {
                List<Card> bestHand = new ArrayList<>(entry.getValue());
                List<Card.Rank> kickers = cards.stream()
                    .filter(c -> c.getRank() != entry.getKey())
                    .map(Card::getRank)
                    .sorted((r1, r2) -> Integer.compare(r2.getValue(), r1.getValue()))
                    .limit(2)
                    .collect(Collectors.toList());
                return new HandResult(HandRank.THREE_OF_A_KIND, bestHand, kickers, 0);
            }
        }
        return null;
    }

    private HandResult checkTwoPair(List<Card> cards) {
        Map<Card.Rank, List<Card>> rankGroups = cards.stream()
            .collect(Collectors.groupingBy(Card::getRank));

        List<Map.Entry<Card.Rank, List<Card>>> pairs = rankGroups.entrySet().stream()
            .filter(e -> e.getValue().size() >= 2)
            .sorted((e1, e2) -> Integer.compare(e2.getKey().getValue(), e1.getKey().getValue()))
            .collect(Collectors.toList());

        if (pairs.size() >= 2) {
            List<Card> bestHand = new ArrayList<>();
            bestHand.addAll(pairs.get(0).getValue().subList(0, 2));
            bestHand.addAll(pairs.get(1).getValue().subList(0, 2));

            List<Card.Rank> kickers = new ArrayList<>();
            kickers.add(pairs.get(0).getKey());
            kickers.add(pairs.get(1).getKey());

            // 添加最大的踢脚牌
            cards.stream()
                .filter(c -> c.getRank() != pairs.get(0).getKey() && 
                           c.getRank() != pairs.get(1).getKey())
                .max((c1, c2) -> Integer.compare(c1.getRank().getValue(), c2.getRank().getValue()))
                .ifPresent(c -> kickers.add(c.getRank()));

            return new HandResult(HandRank.TWO_PAIR, bestHand, kickers, 0);
        }
        return null;
    }

    private HandResult checkPair(List<Card> cards) {
        Map<Card.Rank, List<Card>> rankGroups = cards.stream()
            .collect(Collectors.groupingBy(Card::getRank));

        for (Map.Entry<Card.Rank, List<Card>> entry : rankGroups.entrySet()) {
            if (entry.getValue().size() == 2) {
                List<Card> bestHand = new ArrayList<>(entry.getValue());
                List<Card.Rank> kickers = cards.stream()
                    .filter(c -> c.getRank() != entry.getKey())
                    .map(Card::getRank)
                    .sorted((r1, r2) -> Integer.compare(r2.getValue(), r1.getValue()))
                    .limit(3)
                    .collect(Collectors.toList());
                return new HandResult(HandRank.PAIR, bestHand, kickers, 0);
            }
        }
        return null;
    }

    private HandResult checkHighCard(List<Card> cards) {
        List<Card> bestHand = cards.stream()
            .sorted((c1, c2) -> Integer.compare(c2.getRank().getValue(), c1.getRank().getValue()))
            .limit(5)
            .collect(Collectors.toList());
        List<Card.Rank> kickers = bestHand.stream()
            .map(Card::getRank)
            .collect(Collectors.toList());
        return new HandResult(HandRank.HIGH_CARD, bestHand, kickers, calculateHighCardScore(bestHand));
    }

    private int calculateHighCardScore(List<Card> cards) {
        // 实现高牌分数计算逻辑
        return 0; // 临时返回，需要实现具体逻辑
    }
} 