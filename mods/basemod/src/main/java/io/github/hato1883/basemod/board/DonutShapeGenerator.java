package io.github.hato1883.basemod.board;

import io.github.hato1883.api.game.board.BoardGenerationConfig;
import io.github.hato1883.api.game.board.ICubeCoord;
import io.github.hato1883.api.game.board.IShapeGenerator;
import io.github.hato1883.core.game.board.CubeCoord;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DonutShapeGenerator implements IShapeGenerator {
    @Override
    public Set<ICubeCoord> generateCoords(BoardGenerationConfig config, Random rng) {
        int outerRadius = Math.min(config.getxExtent(), Math.min(config.getyExtent(), config.getzExtent())) / 2 + 1;
        int innerRadius = 2;
        Set<ICubeCoord> coords = new HashSet<>();
        for (int x = -outerRadius; x <= outerRadius; x++)
            for (int y = -outerRadius; y <= outerRadius; y++) {
                int z = -x - y;
                int distanceFromCenter = (Math.abs(x) + Math.abs(y) + Math.abs(z)) / 2;
                if (distanceFromCenter >= innerRadius && distanceFromCenter < outerRadius) {
                    coords.add(new CubeCoord(x, y, z));
                }
            }
        return coords;
    }
}
