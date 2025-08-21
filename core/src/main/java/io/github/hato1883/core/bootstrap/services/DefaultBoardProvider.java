package io.github.hato1883.core.bootstrap.services;

import io.github.hato1883.api.world.board.BoardProvider;
import io.github.hato1883.api.world.board.IBoard;

/**
 * Default implementation of BoardProvider for runtime board management.
 */
public class DefaultBoardProvider implements BoardProvider {
    private IBoard board;
    @Override
    public void setBoard(IBoard board) { this.board = board; }
    @Override
    public IBoard getBoard() { return board; }
}

