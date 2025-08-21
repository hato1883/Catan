package io.github.hato1883.api.events.lifecycle;

import io.github.hato1883.api.world.IGameState;

/**
 * Base class for all round-related events.
 * Carries the round number and game state.
 */
public abstract class RoundEvent extends GameLifecycleEvent {
    private final int roundNumber;

    protected RoundEvent(int roundNumber, IGameState gameState) {
        super(gameState);
        this.roundNumber = roundNumber;
    }

    public int getRoundNumber() {
        return roundNumber;
    }
}

