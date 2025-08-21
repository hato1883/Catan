package io.github.hato1883.api.world.board;

import java.util.Random;
import java.util.Set;

public interface IShapeGenerator {
    Set<ITilePosition> generateCoords(BoardGenerationConfig config, Random rng);
}
