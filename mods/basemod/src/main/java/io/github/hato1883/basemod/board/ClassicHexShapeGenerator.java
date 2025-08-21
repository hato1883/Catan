package io.github.hato1883.basemod.board;

import io.github.hato1883.api.world.board.BoardGenerationConfig;
import io.github.hato1883.api.world.board.IShapeGenerator;
import io.github.hato1883.api.world.board.ITilePosition;
import io.github.hato1883.api.world.board.TilePosition;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Generates a classic Catan hexagon island shape (radius 2 by default).
 */
public class ClassicHexShapeGenerator implements IShapeGenerator {
    @Override
    public Set<ITilePosition> generateCoords(BoardGenerationConfig config, Random rng) {
        int radius = Math.min(config.getxExtent(), Math.min(config.getyExtent(), config.getzExtent())) / 2;
        Set<ITilePosition> coords = new HashSet<>();
        float sqrt3over2 = (float)(Math.sqrt(3.0) / 2.0);
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                int z = -x - y;
                if (Math.abs(z) <= radius) {
                    // Adjust for pointy-topped hex: output in tile-width units
                    float relX = (float)(Math.sqrt(3.0) / 2.0) * (x + z / 2.0f);
                    float relY = 0.75f * z;
                    coords.add(new TilePosition(relX, relY, 0));
                }
            }
        }
        return coords;
    }
}
