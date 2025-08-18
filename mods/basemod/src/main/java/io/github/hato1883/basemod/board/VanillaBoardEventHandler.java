package io.github.hato1883.basemod.board;

import io.github.hato1883.api.events.EventListener;
import io.github.hato1883.api.events.board.PreBoardGenerationEvent;
import io.github.hato1883.api.game.board.IBoard;
import io.github.hato1883.core.game.board.BoardUtils;

public class VanillaBoardEventHandler {
    @EventListener
    public static void generateBoard(PreBoardGenerationEvent event) {
        if (event.isCanceled()) return;

        IBoard board = event.getBoard();

        // Set metadata
        board.setName("Settlers of Catan Classic Board");
        board.setDimensions(BoardUtils.computeDimensionsFromTiles(board.getTiles()));

    }
}
