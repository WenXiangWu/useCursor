package com.poker.ui;

import com.poker.model.Card;
import com.poker.model.Player;
import com.poker.model.Table;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import java.util.List;

public class GameView extends VBox {
    private Table table;
    private Player currentPlayer;
    private HBox communityCards;
    private HBox playerCards;
    private VBox playerInfo;
    private HBox actionButtons;
    private Label potLabel;
    private Label currentBetLabel;

    public GameView(Table table, Player currentPlayer) {
        this.table = table;
        this.currentPlayer = currentPlayer;
        
        initializeUI();
    }

    private void initializeUI() {
        // 社区牌区域
        communityCards = new HBox(10);
        communityCards.getStyleClass().add("community-cards");
        
        // 玩家手牌区域
        playerCards = new HBox(10);
        playerCards.getStyleClass().add("player-cards");
        
        // 玩家信息区域
        playerInfo = new VBox(5);
        playerInfo.getStyleClass().add("player-info");
        playerInfo.getChildren().addAll(
            new Label("玩家: " + currentPlayer.getUsername()),
            new Label("筹码: " + currentPlayer.getChips())
        );
        
        // 游戏信息区域
        VBox gameInfo = new VBox(5);
        gameInfo.getStyleClass().add("table-info");
        potLabel = new Label("底池: 0");
        currentBetLabel = new Label("当前下注: 0");
        gameInfo.getChildren().addAll(potLabel, currentBetLabel);
        
        // 操作按钮区域
        actionButtons = new HBox(10);
        actionButtons.getStyleClass().add("action-buttons");
        actionButtons.getChildren().addAll(
            createActionButton("弃牌", "fold"),
            createActionButton("跟注", "call"),
            createActionButton("加注", "raise")
        );
        
        getChildren().addAll(communityCards, gameInfo, playerCards, playerInfo, actionButtons);
        getStyleClass().add("game-view");
    }

    private Button createActionButton(String text, String action) {
        Button button = new Button(text);
        button.getStyleClass().add("action-button");
        button.setOnAction(e -> handleAction(action));
        return button;
    }

    private void handleAction(String action) {
        // 处理玩家操作
        switch (action) {
            case "fold":
                // 实现弃牌逻辑
                break;
            case "call":
                // 实现跟注逻辑
                break;
            case "raise":
                // 实现加注逻辑
                break;
        }
    }

    public void updateCommunityCards(List<Card> cards) {
        communityCards.getChildren().clear();
        for (Card card : cards) {
            ImageView cardView = createCardView(card);
            addCardAnimation(cardView);
            communityCards.getChildren().add(cardView);
        }
    }

    public void updatePlayerCards(List<Card> cards) {
        playerCards.getChildren().clear();
        for (Card card : cards) {
            ImageView cardView = createCardView(card);
            addCardAnimation(cardView);
            playerCards.getChildren().add(cardView);
        }
    }

    private ImageView createCardView(Card card) {
        ImageView cardView = new ImageView(CardImageManager.getCardImage(card));
        cardView.setFitWidth(100);
        cardView.setFitHeight(140);
        cardView.getStyleClass().add("card-view");
        return cardView;
    }

    private void addCardAnimation(ImageView cardView) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), cardView);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    public void updatePot(int amount) {
        potLabel.setText("底池: " + amount);
    }

    public void updateCurrentBet(int amount) {
        currentBetLabel.setText("当前下注: " + amount);
    }

    public void updatePlayerChips(int chips) {
        ((Label)playerInfo.getChildren().get(1)).setText("筹码: " + chips);
    }
}