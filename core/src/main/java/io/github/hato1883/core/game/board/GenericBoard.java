package io.github.hato1883.core.game.board;

import io.github.hato1883.api.game.board.Dimension;

public class GenericBoard extends AbstractIBoardImpl {
    GenericBoard(String name, Dimension dimension) {
        setName(name);
        setDimensions(dimension);
    }
}
