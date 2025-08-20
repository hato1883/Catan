package io.github.hato1883.api.ui;

import io.github.hato1883.api.world.board.IBoard;

public interface IGUI {

    /**
     * Initialize the GUI with a game board.
     */
    void init(IBoard gameBoard);

    /**
     * Start the GUI loop / rendering.
     * For LibGDX, this would trigger `ApplicationListener` internally.
     */
    void run();

    /**
     * Signal the GUI to close gracefully.
     */
    void close();

    /**
     * Whether the GUI is currently running.
     */
    boolean isRunning();
}
