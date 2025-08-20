package io.github.hato1883.core.game.world.board;

import io.github.hato1883.api.world.board.BoardGenerationConfig;
import io.github.hato1883.api.world.board.ICubeCoord;
import io.github.hato1883.api.world.board.IShapeGenerator;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class HexIslandShape implements IShapeGenerator {
    @Override
    public Set<ICubeCoord> generateCoords(BoardGenerationConfig config, Random rng) {
        // Generate hexagonal island coordinates based on radiusX/Y/Z
        int radius = Math.min(config.getxExtent(), Math.min(config.getyExtent(), config.getzExtent())) / 2 + 1;
        Set<ICubeCoord> coords = new HashSet<>();
        for (int x = -radius + 1; x <= radius - 1; x++) {
            for (int y = -radius + 1; y <= radius - 1; y++) {
                int z = -x - y;
                if (Math.abs(z) < radius) {
                    coords.add(new CubeCoord(x, y, z));
                }
            }
        }
        return coords;
    }
}
