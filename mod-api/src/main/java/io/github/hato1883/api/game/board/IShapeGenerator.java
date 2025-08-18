package io.github.hato1883.api.game.board;

import java.util.Random;
import java.util.Set;

public interface IShapeGenerator {
    Set<ICubeCoord> generateCoords(BoardGenerationConfig config, Random rng);
}
