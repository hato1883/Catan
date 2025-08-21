package io.github.hato1883.api.events.lifecycle;

import io.github.hato1883.api.world.IGameState;

public class RoundStartEvent extends RoundEvent {
    public RoundStartEvent(int roundNumber, IGameState gameState) {
        super(roundNumber, gameState);
    }
}
