package io.github.hato1883.api.events.board;

import io.github.hato1883.api.events.IEvent;
import io.github.hato1883.api.world.board.BoardGenerationConfig;
import io.github.hato1883.api.world.board.IBoard;

import java.util.Random;

public class BoardGenerationEvent implements IEvent {
    protected final IBoard board;
    protected final BoardGenerationConfig config;
    protected final Random rng;

    public BoardGenerationEvent(IBoard board, BoardGenerationConfig config, Random rng) {
        this.board = board;
        this.config = config;
        this.rng = rng;
    }

    /**
     * Board after all tiles have been placed
     */
    public IBoard getBoard() {
        return board;
    }

    /**
     * Modifiable configuration for generation, e.g., dimensions, shuffle rules
     */
    public BoardGenerationConfig getConfig() {
        return config;
    }

    /**
     * Modifiable configuration for generation, e.g., dimensions, shuffle rules
     */
    public Random getRandomness() {
        return rng;
    }
}
