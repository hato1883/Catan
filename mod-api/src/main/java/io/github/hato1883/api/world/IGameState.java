package io.github.hato1883.api.world;

import io.github.hato1883.api.world.board.IBoard;
import io.github.hato1883.api.world.phase.IGamePhase;
import io.github.hato1883.api.entities.player.IPlayer;

import java.util.List;

public interface IGameState {
    List<IPlayer> getPlayers();
    IPlayer getCurrentPlayer();
    IBoard getBoard();
    IGamePhase getCurrentPhase(); // enum or interface
    void advancePhase();
}
