package io.github.hato1883.api.game;

public interface IPhaseLogic {
    IGamePhase getPhase();
    void execute(IGameState state);
    boolean isComplete(IGameState state);
}
