package io.github.hato1883.basemod.board.logic;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.world.board.*;

import static io.github.hato1883.basemod.BaseModMain.MOD_ID;

public class ShuffledHexIslandBoard extends ClassicHexIslandBoard {

    public ShuffledHexIslandBoard() {
        super(Identifier.of(MOD_ID, "classic_hex_shuffled"), "Classic Hex Island (Shuffled Tokens)");
    }

    @Override
    public BoardGenerationConfig getDefaultConfig() {
        // Enable shuffling by default
        return new BoardGenerationConfig(3, true);
    }
}
