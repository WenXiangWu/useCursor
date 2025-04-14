package com.poker.ui;

import com.poker.model.Card;
import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;

public class CardImageManager {
    private static final String CARD_IMAGE_PATH = "/static/images/cards/";
    private static final Map<String, Image> cardImages = new HashMap<>();
    private static final Image cardBack;

    static {
        // 加载卡牌背面图片
        cardBack = new Image(CardImageManager.class.getResourceAsStream(CARD_IMAGE_PATH + "back.png"));
        
        // 预加载所有卡牌图片
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                String imagePath = CARD_IMAGE_PATH + getCardImageName(suit, rank);
                cardImages.put(getCardKey(suit, rank), 
                    new Image(CardImageManager.class.getResourceAsStream(imagePath)));
            }
        }
    }

    public static Image getCardImage(Card card) {
        return cardImages.get(getCardKey(card.getSuit(), card.getRank()));
    }

    public static Image getCardBack() {
        return cardBack;
    }

    private static String getCardKey(Card.Suit suit, Card.Rank rank) {
        return suit.name() + "_" + rank.name();
    }

    private static String getCardImageName(Card.Suit suit, Card.Rank rank) {
        String rankStr = rank.name().toLowerCase();
        String suitStr = suit.name().toLowerCase();
        return rankStr + "_of_" + suitStr + ".png";
    }
}