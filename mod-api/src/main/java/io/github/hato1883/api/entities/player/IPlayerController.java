package io.github.hato1883.api.entities.player;

import io.github.hato1883.api.world.IGameState;

public interface IPlayerController {
    void takeTurn(IGameState gameState);
}
