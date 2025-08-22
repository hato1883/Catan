package io.github.hato1883.basemod.board.logic;

import io.github.hato1883.api.world.board.BoardGenerationConfig;
import io.github.hato1883.api.world.board.IShapeGenerator;
import io.github.hato1883.api.world.board.ITilePosition;
import io.github.hato1883.api.world.board.TilePosition;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SquareBoardGenerator implements IShapeGenerator {
    @Override
    public Set<ITilePosition> generateCoords(BoardGenerationConfig config, Random rng) {
        int radius = Math.min(config.getxExtent(), Math.min(config.getyExtent(), config.getzExtent())) / 2;
        Set<ITilePosition> coords = new HashSet<>();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                // Output positions in tile-width units (relative)
                coords.add(new TilePosition(x, y, 0));
            }
        }
        return coords;
    }
}
