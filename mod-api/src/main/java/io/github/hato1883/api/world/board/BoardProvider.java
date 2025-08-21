package io.github.hato1883.api.world.board;

/**
 * Provides access to the current IBoard instance, allowing it to be set or updated at runtime.
 */
public interface BoardProvider {
    void setBoard(IBoard board);
    IBoard getBoard();
}

