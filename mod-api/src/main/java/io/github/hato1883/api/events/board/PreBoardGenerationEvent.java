package io.github.hato1883.api.events.board;

import io.github.hato1883.api.events.IEvent;
import io.github.hato1883.api.world.board.BoardGenerationConfig;
import io.github.hato1883.api.world.board.IBoard;

import java.util.Random;

public class PreBoardGenerationEvent extends BoardGenerationEvent implements IEvent {

    private boolean canceled = false;

    public PreBoardGenerationEvent(IBoard board, BoardGenerationConfig config, Random rng) {
        super(board, config, rng);
    }

    /** Cancel generation entirely */
    public void cancel() {
        this.canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }
}

