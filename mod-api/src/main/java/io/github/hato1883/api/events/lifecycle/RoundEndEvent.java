package io.github.hato1883.api.events.lifecycle;

import io.github.hato1883.api.world.IGameState;

public class RoundEndEvent extends RoundEvent {
    public RoundEndEvent(int roundNumber, IGameState gameState) {
        super(roundNumber, gameState);
    }
}
