package io.github.hato1883.core.game.world.board;

import io.github.hato1883.api.world.board.Dimension;

public class GenericBoard extends AbstractIBoard {
    GenericBoard(String name, Dimension dimension) {
        setName(name);
        setDimensions(dimension);
    }
}
