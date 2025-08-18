package io.github.hato1883.api.game.board;

import io.github.hato1883.api.Identifier;

import java.util.Random;

public interface IBoardGenerator {
    IBoard generateBoard(IBoardType type, BoardGenerationConfig config, Random rng);
    IBoard generateBoard(Identifier id, BoardGenerationConfig config, Random rng);
    IBoard generateBoard(Identifier id, Random rng);
}
