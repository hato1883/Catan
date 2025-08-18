package io.github.hato1883.api.ui;

import io.github.hato1883.api.game.board.IBoard;

public interface ITextUI {

    /**
     * Initialize the UI with the given game board.
     * This allows dependency injection of the board logic.
     */
    void init(IBoard gameBoard);

    /**
     * Start the UI loop.
     * Should not block indefinitely in a way that prevents graceful shutdown.
     */
    void run();

    /**
     * Signal the UI to close.
     */
    void close();

    /**
     * Optional: Check if UI wants to continue running.
     * Default: true
     */
    boolean isRunning();
}
