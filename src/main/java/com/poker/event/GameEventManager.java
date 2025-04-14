package com.poker.event;

import com.poker.event.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GameEventManager {
    private final List<GameEventListener> listeners = new ArrayList<>();
    private final ApplicationEventPublisher eventPublisher;

    public GameEventManager(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void addEventListener(GameEventListener listener) {
        listeners.add(listener);
    }

    public void removeEventListener(GameEventListener listener) {
        listeners.remove(listener);
    }

    public void fireEvent(GameStartedEvent event) {
        eventPublisher.publishEvent(event);
        for (GameEventListener listener : listeners) {
            listener.onGameStarted(event);
        }
    }

    public void fireEvent(GameEndedEvent event) {
        eventPublisher.publishEvent(event);
        for (GameEventListener listener : listeners) {
            listener.onGameEnded(event);
        }
    }

    public void fireEvent(PlayerJoinedEvent event) {
        eventPublisher.publishEvent(event);
        for (GameEventListener listener : listeners) {
            listener.onPlayerJoined(event);
        }
    }

    public void fireEvent(PlayerLeftEvent event) {
        eventPublisher.publishEvent(event);
        for (GameEventListener listener : listeners) {
            listener.onPlayerLeft(event);
        }
    }

    public void fireEvent(RoundStartedEvent event) {
        eventPublisher.publishEvent(event);
        for (GameEventListener listener : listeners) {
            listener.onRoundStarted(event);
        }
    }

    public void fireEvent(PlayerActionEvent event) {
        eventPublisher.publishEvent(event);
        for (GameEventListener listener : listeners) {
            listener.onPlayerAction(event);
        }
    }

    public void fireEvent(RoundEndedEvent event) {
        eventPublisher.publishEvent(event);
        for (GameEventListener listener : listeners) {
            listener.onRoundEnded(event);
        }
    }

    public void fireEvent(CardDealtEvent event) {
        eventPublisher.publishEvent(event);
        for (GameEventListener listener : listeners) {
            listener.onCardDealt(event);
        }
    }

    public void fireEvent(CommunityCardDealtEvent event) {
        eventPublisher.publishEvent(event);
        for (GameEventListener listener : listeners) {
            listener.onCommunityCardDealt(event);
        }
    }

    public void fireEvent(PlayerEliminatedEvent event) {
        eventPublisher.publishEvent(event);
        for (GameEventListener listener : listeners) {
            listener.onPlayerEliminated(event);
        }
    }

    public void fireEvent(GameStateChangedEvent event) {
        eventPublisher.publishEvent(event);
        for (GameEventListener listener : listeners) {
            listener.onGameStateChanged(event);
        }
    }

    public void fireEvent(BlindsChangedEvent event) {
        eventPublisher.publishEvent(event);
        for (GameEventListener listener : listeners) {
            listener.onBlindsChanged(event);
        }
    }

    public void fireEvent(DealerChangedEvent event) {
        eventPublisher.publishEvent(event);
        for (GameEventListener listener : listeners) {
            listener.onDealerChanged(event);
        }
    }

    public void fireEvent(PlayerTimeoutEvent event) {
        eventPublisher.publishEvent(event);
        for (GameEventListener listener : listeners) {
            listener.onPlayerTimeout(event);
        }
    }

    public void fireEvent(ChatMessageEvent event) {
        eventPublisher.publishEvent(event);
        for (GameEventListener listener : listeners) {
            listener.onChatMessage(event);
        }
    }

    public void fireEvent(BetPlacedEvent event) {
        eventPublisher.publishEvent(event);
        for (GameEventListener listener : listeners) {
            listener.onBetPlaced(event);
        }
    }

    public void fireEvent(HandStartedEvent event) {
        eventPublisher.publishEvent(event);
        for (GameEventListener listener : listeners) {
            listener.onHandStarted(event);
        }
    }

    public void fireEvent(CardsDealtEvent event) {
        eventPublisher.publishEvent(event);
        for (GameEventListener listener : listeners) {
            listener.onCardsDealt(event);
        }
    }

    public void fireEvent(CommunityCardsDealtEvent event) {
        eventPublisher.publishEvent(event);
        for (GameEventListener listener : listeners) {
            listener.onCommunityCardsDealt(event);
        }
    }

    public void fireEvent(PlayerFoldedEvent event) {
        eventPublisher.publishEvent(event);
        for (GameEventListener listener : listeners) {
            listener.onPlayerFolded(event);
        }
    }
}