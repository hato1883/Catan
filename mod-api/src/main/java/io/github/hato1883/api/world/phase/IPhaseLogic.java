package io.github.hato1883.api.world.phase;

import io.github.hato1883.api.world.IGameState;

public interface IPhaseLogic {
    IGamePhase getPhase();
    void execute(IGameState state);
    boolean isComplete(IGameState state);
}
