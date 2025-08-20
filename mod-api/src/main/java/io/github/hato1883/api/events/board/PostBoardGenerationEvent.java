package io.github.hato1883.api.events.board;

import io.github.hato1883.api.world.board.BoardGenerationConfig;
import io.github.hato1883.api.world.board.IBoard;

import java.util.Random;

public class PostBoardGenerationEvent extends BoardGenerationEvent {

    public PostBoardGenerationEvent(IBoard board, BoardGenerationConfig config, Random rng) {
        super(board, config, rng);
    }

    /** Convenience: rename the board for display purposes */
    public void setBoardName(String name) {
        board.setName(name);
    }
}
