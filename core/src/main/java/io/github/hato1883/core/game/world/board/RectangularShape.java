package io.github.hato1883.core.game.world.board;


import io.github.hato1883.api.world.board.BoardGenerationConfig;
import io.github.hato1883.api.world.board.ICubeCoord;
import io.github.hato1883.api.world.board.IShapeGenerator;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RectangularShape implements IShapeGenerator {
    @Override
    public Set<ICubeCoord> generateCoords(BoardGenerationConfig config, Random rng) {
        // Generate rectangular board
        int half_width = config.getxExtent();
        int half_height = (config.getyExtent() + config.getzExtent()) / 2 / 2;
        Set<ICubeCoord> coords = new HashSet<>();
        for (int x = -half_width + 1; x <= half_width - 1; x++) {
            for (int y = -half_height + 1; y <= half_height - 1; y++) {
                int z = -x - y;
                coords.add(new CubeCoord(x, y, z));
            }
        }
        return coords;
    }
}
