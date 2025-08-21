package io.github.hato1883.api.events.lifecycle;

import io.github.hato1883.api.events.IEvent;
import io.github.hato1883.api.world.IGameState;

public class GameStartEvent extends GameLifecycleEvent {
    // Add fields if needed (e.g., game context)
    public GameStartEvent(IGameState gameState) {
        super(gameState);
    }
}

