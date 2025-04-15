package com.poker.event;

import com.poker.model.Game;
import com.poker.model.Player;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PlayerActionEvent extends GameEvent {
    private final String gameId;
    private final Player player;
    private final String action;  // FOLD, CHECK, CALL, RAISE, ALL_IN
    private final int amount;     // 下注或加注金额
    private final int currentBet; // 当前下注额
    private final int potSize;    // 当前奖池大小
    private final String nextPlayerId;  // 下一个行动玩家
    private final long actionTime;      // 行动时间戳

    public PlayerActionEvent(Object source, Game game, Player player, String action, int amount) {
        super(source, game, player, GameEventType.PLAYER_ACTION, action);
        this.gameId = game.getId();
        this.player = player;
        this.action = action;
        this.amount = amount;
        this.currentBet = 0; // Assuming currentBet is not provided in the constructor
        this.potSize = 0; // Assuming potSize is not provided in the constructor
        this.nextPlayerId = null; // Assuming nextPlayerId is not provided in the constructor
        this.actionTime = System.currentTimeMillis(); // Assuming actionTime is not provided in the constructor
    }

    @Override
    public Game getGame() {
        return super.getGame();
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}