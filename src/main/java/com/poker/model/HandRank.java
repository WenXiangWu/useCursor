package com.poker.model;

public enum HandRank {
    HIGH_CARD(1, "高牌"),
    PAIR(2, "一对"),
    TWO_PAIR(3, "两对"),
    THREE_OF_A_KIND(4, "三条"),
    STRAIGHT(5, "顺子"),
    FLUSH(6, "同花"),
    FULL_HOUSE(7, "葫芦"),
    FOUR_OF_A_KIND(8, "四条"),
    STRAIGHT_FLUSH(9, "同花顺"),
    ROYAL_FLUSH(10, "皇家同花顺");

    private final int value;
    private final String description;

    HandRank(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
} 