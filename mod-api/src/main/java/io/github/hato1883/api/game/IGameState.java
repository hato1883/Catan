package io.github.hato1883.api.game;

import io.github.hato1883.api.game.board.IBoard;

import java.util.List;

public interface IGameState {
    List<IPlayer> getPlayers();
    IPlayer getCurrentPlayer();
    IBoard getBoard();
    IGamePhase getCurrentPhase(); // enum or interface
    void advancePhase();
}
